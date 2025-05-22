package com.example.seyahatnamem.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seyahatnamem.R
import com.example.seyahatnamem.adapter.GezilerimAdapter
import com.example.seyahatnamem.databinding.FragmentGeziDetayBinding
import com.example.seyahatnamem.databinding.FragmentSehirDetayBinding
import com.example.seyahatnamem.model.Gezi
import com.squareup.picasso.Picasso


class GeziDetayFragment : Fragment() {
    private var _binding: FragmentGeziDetayBinding? = null
    private val binding get() = _binding!!
    private lateinit var secilenGezi : Gezi


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGeziDetayBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let{
            secilenGezi = GeziDetayFragmentArgs.fromBundle(it).geziListe
        }

        Picasso.get().load(secilenGezi.gorselUrl).into(binding.geziFoto)
        binding.geziYorum.text = secilenGezi.yorum
        binding.geziAdi.text = secilenGezi.geziAdi


    }



}