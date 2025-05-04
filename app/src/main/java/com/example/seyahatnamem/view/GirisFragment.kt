package com.example.seyahatnamem.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.seyahatnamem.databinding.FragmentGirisBinding


class GirisFragment : Fragment() {
    private var _binding: FragmentGirisBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    }



    //Giriş Yap butonuna tıklandığında seyahatlerim sayfasına gitmek için oluşturuldu
    fun girisYap(view: View){
        val action = GirisFragmentDirections.actionGirisFragmentToSeyahatlerimFragment()
        Navigation.findNavController(view).navigate(action)
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