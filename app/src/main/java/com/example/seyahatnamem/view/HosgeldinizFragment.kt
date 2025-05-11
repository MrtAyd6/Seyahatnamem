package com.example.seyahatnamem.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.seyahatnamem.databinding.FragmentHosgeldinizBinding

class HosgeldinizFragment : Fragment() {
    private var _binding: FragmentHosgeldinizBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHosgeldinizBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cikisYapButonu.setOnClickListener{
            cikisYap(it)    //// Çıkış Yap butonuna bastığında ana sayfaya yönlendirmesi için buton oluşturuldu
        }
        val kullaniciAdi = "beyza6161" //ILERIDE CLASS'TAN ALINACAK!!!!!!
        binding.kullaniciAdiniGosterenText.text = "Kullanıcı Adı : ${kullaniciAdi}"

        binding.parolayGosterenText.text = "Parola :  ********"


    }





    fun cikisYap(view: View){
        val action = HosgeldinizFragmentDirections.actionHosgeldinizFragmentToGirisFragment()
        Navigation.findNavController(view).navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}