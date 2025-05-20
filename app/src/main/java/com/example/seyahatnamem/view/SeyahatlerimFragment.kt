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
import com.example.seyahatnamem.model.Sehir
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore


class SeyahatlerimFragment : Fragment() {
    private var _binding: FragmentSeyahatlerimBinding? = null
    private val binding get() = _binding!!
    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    val sehirList : ArrayList<Sehir> = arrayListOf()
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
                        adapter?.notifyDataSetChanged() //adaptörü güncellemek icin
                    }
                }
            }
        }
    }



    private fun seyahatEkle(view: View){
        val action = SeyahatlerimFragmentDirections.actionSeyahatlerimFragmentToSehirEkleFragment()
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


}