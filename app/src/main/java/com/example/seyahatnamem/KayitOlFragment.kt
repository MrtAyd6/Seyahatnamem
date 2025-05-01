package com.example.seyahatnamem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.seyahatnamem.databinding.FragmentGirisBinding
import com.example.seyahatnamem.databinding.FragmentKayitOlBinding


class KayitOlFragment : Fragment() {
    private var _binding: FragmentKayitOlBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            giriseDon(it)        //Girişe dön butonuna basıldıında ana sayfaya yönlendirilmesi için buton oluşturuldu
        }


    }




    fun giriseDon(view: View){
        val action = KayitOlFragmentDirections.actionKayitOlFragmentToGirisFragment()
        Navigation.findNavController(view).navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}