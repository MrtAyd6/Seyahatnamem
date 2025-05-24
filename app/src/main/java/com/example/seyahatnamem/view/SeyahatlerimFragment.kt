package com.example.seyahatnamem.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seyahatnamem.adapter.SeyahatlerimAdapter
import com.example.seyahatnamem.databinding.FragmentSeyahatlerimBinding
import com.example.seyahatnamem.model.Gezi
import com.example.seyahatnamem.model.Kullanici
import com.example.seyahatnamem.model.Sehir
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.squareup.picasso.Picasso
import java.text.Collator
import java.util.Locale


class SeyahatlerimFragment : Fragment() {
    private var _binding: FragmentSeyahatlerimBinding? = null
    private val binding get() = _binding!!
    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    val sehirList : ArrayList<Sehir> = arrayListOf()
    //var sehirListesi = arrayOf<String>() // sehir ekle fragment'a gidip halihazırda ekli olan bir sehir adının tekrardan eklenmesini engellemek icin tanımlandı.
    //!!!! sehirList ' i neden kullanmadık diye sorarsanız cünkü sehirList'in veritipi nası olduysa sehireklefragment'e gonderilmeye musait degildi.
    private var adapter : SeyahatlerimAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSeyahatlerimBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.seyahatEkleButton.setOnClickListener { seyahatEkle(it) }

        binding.profilSayfasinaGitButonu.setOnClickListener {
            profileGit(it)
        }
        firestoreVerileriAl()
        firestorePPVerileriAl()
        adapter = SeyahatlerimAdapter(sehirList)
        binding.seyahatRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.seyahatRecycler.adapter = adapter

    }

    private  fun firestoreVerileriAl(){

        val targetEmail = FirebaseAuth.getInstance().currentUser?.email
        db.collection("Sehirler").whereEqualTo("ID",targetEmail).addSnapshotListener { value, error ->
            if(error != null){
                Toast.makeText(requireContext(),error.localizedMessage, Toast.LENGTH_LONG).show()
            }else{
                println("email :"+targetEmail)
                if(value != null){
                    if(!value.isEmpty){
                        //boş değilse
                        sehirList.clear()
                        val documents = value.documents
                        for(document in documents){
                            val sehirAdi = document.get("Sehir_adi") as String
                            val geziList = arrayListOf<Gezi>()
                            val sehir = Sehir(sehirAdi,geziList)
                            Log.d("SEHIREKLEME","EKLENEN SEHIR BILGILERI" + sehirAdi)
                            sehirList.add(sehir)
                        }
                        val language = Collator.getInstance(Locale("tr","TR")) //Türkçe dil desteği sağlamak için
                        sehirList.sortWith(compareBy(language){it.sehirAdi})//Şehir listesini Türkçe alfabetik sırasına göre ayarlar
                        adapter?.notifyDataSetChanged() //adaptörü güncellemek icin

                    }
                }
            }
        }
    }

    private fun seyahatEkle(view: View){
        //Sehir ekleme fragment'ine daha onceden eklenmis olan sehirlerin listesini gonderiyoruz.
        var sehirListesi = sehirList.map { it.sehirAdi }.toTypedArray()
        val action = SeyahatlerimFragmentDirections.actionSeyahatlerimFragmentToSehirEkleFragment(sehirListesi) //recycler view'daki sehir listesini sehir ekleye gönderip aynı sehrin iki kere listelenmesini engelleyecegiz!
        Navigation.findNavController(view).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun profileGit(view: View){
        val action = SeyahatlerimFragmentDirections.actionSeyahatlerimFragmentToHosgeldinizFragment()
        Navigation.findNavController(view).navigate(action)
    }
    private  fun firestorePPVerileriAl(){
        val kullaniciBilgiListesi = ArrayList<Kullanici>()
        val targetEmail = FirebaseAuth.getInstance().currentUser?.email
        db.collection("KullaniciBilgi").whereEqualTo("ID",targetEmail).addSnapshotListener { value, error ->
            if(error != null){
                Toast.makeText(requireContext(),error.localizedMessage, Toast.LENGTH_LONG).show()
            }else{
                println("email :"+targetEmail)
                if(value != null){
                    if(!value.isEmpty){
                        //boş değilse
                        kullaniciBilgiListesi.clear()
                        val documents = value.documents
                        for(document in documents){

                            val ppUrl = document.get("ppUrl") as String

                            val kullanici = Kullanici("${targetEmail}","*****",ppUrl)
                            kullaniciBilgiListesi.add(kullanici)
                        }
                        val pp = Picasso.get().load(kullaniciBilgiListesi[0].profilResmi).into(binding.profilSayfasinaGitButonu)
                    }
                }
            }
        }

}}