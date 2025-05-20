package com.example.seyahatnamem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.seyahatnamem.databinding.SeyahatRowBinding
import com.example.seyahatnamem.model.Sehir
import com.example.seyahatnamem.view.SeyahatlerimFragmentDirections
import com.google.firebase.firestore.Query

class SeyahatlerimAdapter(private val sehirListe: ArrayList<Sehir>): RecyclerView.Adapter<SeyahatlerimAdapter.SehirHolder>() {
    class SehirHolder(val binding: SeyahatRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SehirHolder {
        val binding = SeyahatRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SehirHolder(binding)
    }

    override fun getItemCount(): Int {
        return sehirListe.size
    }

    override fun onBindViewHolder(
        holder: SehirHolder,
        position: Int
    ) {
        holder.binding.recyclerSehirAdi.text = sehirListe[position].sehirAdi.toString()


        holder.itemView.setOnClickListener {
            val secilenSehir = sehirListe[position]

            val action = SeyahatlerimFragmentDirections.actionSeyahatlerimFragmentToSehirDetayFragment(secilenSehir)
            Navigation.findNavController(it).navigate(action)
        }
    }








}