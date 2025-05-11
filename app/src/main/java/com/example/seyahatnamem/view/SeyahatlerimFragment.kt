package com.example.seyahatnamem.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.seyahatnamem.databinding.FragmentSeyahatlerimBinding


class SeyahatlerimFragment : Fragment() {
    private var _binding: FragmentSeyahatlerimBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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