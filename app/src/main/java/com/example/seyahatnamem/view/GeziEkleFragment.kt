package com.example.seyahatnamem.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.seyahatnamem.databinding.FragmentGeziEkleBinding

class GeziEkleFragment : Fragment() {
    private var _binding: FragmentGeziEkleBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGeziEkleBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.kaydetButton.setOnClickListener { kaydet(it) }

    }

    private fun kaydet(view: View){
        arguments?.let {
            val secilenSehir = GeziEkleFragmentArgs.fromBundle(it).secilenSehir
            //sehir bilgileri kaydedilecek



            val action = GeziEkleFragmentDirections.actionGeziEkleFragmentToSehirDetayFragment(secilenSehir)
            Navigation.findNavController(view).navigate(action)

        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}