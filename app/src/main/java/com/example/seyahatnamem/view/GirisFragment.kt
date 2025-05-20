package com.example.seyahatnamem.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.seyahatnamem.databinding.FragmentGirisBinding
import com.example.seyahatnamem.model.Kullanici
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class GirisFragment : Fragment() {
    private var _binding: FragmentGirisBinding? = null
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
        _binding = FragmentGirisBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.girisButonu.setOnClickListener {
            girisYap(it)
        }

        binding.kaydolmaButonu.setOnClickListener {
            kaydol(it)
        }


        val guncelKullanici = auth.currentUser
        if(guncelKullanici != null){
            //kullanıcı daha önce giriş yapmış
            val action = GirisFragmentDirections.actionGirisFragmentToSeyahatlerimFragment()
            Navigation.findNavController(view).navigate(action)
        }

    }




    //Giriş Yap butonuna tıklandığında seyahatlerim sayfasına gitmek için oluşturuldu
    fun girisYap(view: View){
        val email = binding.EmailText.text.toString()
        val parola = binding.ParolaText.text.toString()
        if(email.isNotEmpty() && parola.isNotEmpty()){
            auth.signInWithEmailAndPassword(email,parola).addOnSuccessListener {
                val action = GirisFragmentDirections.actionGirisFragmentToSeyahatlerimFragment()
                Navigation.findNavController(view).navigate(action)
            }.addOnFailureListener { exceprion ->
                Toast.makeText(requireContext(),"Email veya şifre yanlış", Toast.LENGTH_LONG).show()
            }
        }
    }

    //Kaydol butonuna tıklandığında kayıt olma sayfasına geçmek için oluşturuldu
    fun kaydol(view: View){
        val action = GirisFragmentDirections.actionGirisFragmentToKayitOlFragment()
        Navigation.findNavController(view).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}