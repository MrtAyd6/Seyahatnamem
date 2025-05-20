package com.example.seyahatnamem.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.seyahatnamem.R
import com.example.seyahatnamem.databinding.FragmentSehirDetayBinding
import com.example.seyahatnamem.databinding.FragmentSeyahatlerimBinding


class SehirDetayFragment : Fragment() {
    private var _binding: FragmentSehirDetayBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSehirDetayBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.geziEkleButton.setOnClickListener { geziEkle(it) }

    }

    private fun geziEkle(view: View){
        arguments?.let {
            val secilenSehir = SehirDetayFragmentArgs.fromBundle(it).secilensehir
            val action = SehirDetayFragmentDirections.actionSehirDetayFragmentToGeziEkleFragment(secilenSehir)
            Navigation.findNavController(view).navigate(action)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}