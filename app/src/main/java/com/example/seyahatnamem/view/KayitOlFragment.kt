package com.example.seyahatnamem.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.seyahatnamem.databinding.FragmentKayitOlBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class KayitOlFragment : Fragment() {
    private var _binding: FragmentKayitOlBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKayitOlBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.giriseDonButonu.setOnClickListener {
            giriseDon(it)        //Girişe dön butonuna basıldıında ana sayfaya yönlendirilmesi için buton oluşturuldu
        }

        binding.KayitKaydetButonu.setOnClickListener {
            kayitOl(it)
            //giriseDon(it)        //Girişe dön butonuna basıldıında ana sayfaya yönlendirilmesi için buton oluşturuldu
        }


    }



    fun kayitOl(view: View){
        val kullaniciAdi = binding.kayitKullaniciAdi.text.toString()
        val parola = binding.kayitParola.text.toString()

        if(kullaniciAdi.isNotEmpty() && parola.isNotEmpty()){
            auth.createUserWithEmailAndPassword(kullaniciAdi,parola).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    //kullanıcı oluşturuldu
                    Toast.makeText(requireContext(),"Kayıt işlemi başarılı", Toast.LENGTH_LONG).show()
                    val action = KayitOlFragmentDirections.actionKayitOlFragmentToGirisFragment()
                    Navigation.findNavController(view).navigate(action)
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(),exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }


    fun giriseDon(view: View){
        val action = KayitOlFragmentDirections.actionKayitOlFragmentToGirisFragment()
        Navigation.findNavController(view).navigate(action)
       // findNavController().navigate(KayitOlFragmentDirections.actionKayitOlFragmentToGirisFragment())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}