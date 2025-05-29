package com.example.seyahatnamem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.seyahatnamem.databinding.GeziRowBinding
import com.example.seyahatnamem.model.Gezi
import com.example.seyahatnamem.model.Sehir
import com.example.seyahatnamem.view.SehirDetayFragmentDirections
import com.example.seyahatnamem.view.SehirEkleFragmentDirections
import com.example.seyahatnamem.view.SeyahatlerimFragmentDirections
import com.squareup.picasso.Picasso

class GezilerimAdapter(private val geziListe: ArrayList<Gezi>, private val secilenSehir: Sehir) : RecyclerView.Adapter<GezilerimAdapter.GeziHolder>() {
    class GeziHolder(val binding: GeziRowBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GeziHolder {
        val binding = GeziRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return GeziHolder(binding)
    }

    override fun getItemCount(): Int {
        return geziListe.size
    }

    override fun onBindViewHolder(
        holder: GeziHolder,
        position: Int
    ) {
        holder.binding.recyclerGeziAdi.text = geziListe[position].geziAdi
        Picasso.get().load(geziListe[position].gorselUrl).into(holder.binding.recyclerGeziKucukGorsel)

        holder.itemView.setOnClickListener {

            val action = SehirDetayFragmentDirections.actionSehirDetayFragmentToGeziDetayFragment(geziListe[position],secilenSehir)
            Navigation.findNavController(it).navigate(action)
        }
    }






}