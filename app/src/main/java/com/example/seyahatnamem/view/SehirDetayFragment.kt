package com.example.seyahatnamem.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seyahatnamem.R
import com.example.seyahatnamem.adapter.GezilerimAdapter
import com.example.seyahatnamem.databinding.FragmentSehirDetayBinding
import com.example.seyahatnamem.databinding.FragmentSeyahatlerimBinding
import com.example.seyahatnamem.model.Gezi
import com.example.seyahatnamem.model.Sehir
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class SehirDetayFragment : Fragment() {
    private var _binding: FragmentSehirDetayBinding? = null
    private val binding get() = _binding!!
    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private lateinit var secilenSehir : Sehir
    private  var geziListe : ArrayList<Gezi> = arrayListOf()
    private lateinit var documentID : String
    private var adapter : GezilerimAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
        auth = Firebase.auth
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
        arguments?.let{
            secilenSehir = SehirDetayFragmentArgs.fromBundle(it).secilensehir
        }
        firestoreVerileriAl()
        binding.geziEkleButton.setOnClickListener { geziEkle(it) }
        binding.sehirSilButton.setOnClickListener { sehirSil(it) }
        adapter = GezilerimAdapter(geziListe,secilenSehir)
        binding.geziRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.geziRecycler.adapter = adapter



    }

    private  fun firestoreVerileriAl() {

        val targetEmail = FirebaseAuth.getInstance().currentUser?.email

        db.collection("Sehirler").whereEqualTo("ID", targetEmail)
            .whereEqualTo("Sehir_adi", secilenSehir.sehirAdi).get()
            .addOnSuccessListener { documents ->

                for (document in documents) {
                    documentID = document.id
                }

                val sehirRef = db.collection("Sehirler").document(documentID)
                sehirRef.collection("geziler").addSnapshotListener { value, error ->
                    if (error != null) {
                        Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                    } else {
                        if (value != null) {
                            if (!value.isEmpty) {
                                //boş değilse
                                geziListe.clear()
                                val documents = value.documents
                                for (document in documents) {
                                    val yorum = document.get("yorum") as String
                                    val gorselUrl = document.get("gorselUrl") as String
                                    val geziAdi = document.get("geziAdi") as String

                                    val gezi = Gezi(geziAdi,yorum, gorselUrl)
                                    geziListe.add(gezi)


                                }
                                adapter?.notifyDataSetChanged() //adaptörü güncellemek icin
                            }
                        }
                    }

                }
            }.addOnFailureListener {
            Toast.makeText(requireContext(), "Sehir bulunamadı!!!", Toast.LENGTH_LONG).show()
        }


    }
    private fun geziEkle(view: View){
        arguments?.let {
            val secilenSehir = SehirDetayFragmentArgs.fromBundle(it).secilensehir
            val action = SehirDetayFragmentDirections.actionSehirDetayFragmentToGeziEkleFragment(secilenSehir)
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun sehirSil(view: View) {
        val targetEmail = FirebaseAuth.getInstance().currentUser?.email

        db.collection("Sehirler").whereEqualTo("ID", targetEmail)
            .whereEqualTo("Sehir_adi", secilenSehir.sehirAdi).get()
            .addOnSuccessListener { documents ->

                for (document in documents) {
                    documentID = document.id
                }

                db.collection("Sehirler").document(documentID).delete().addOnSuccessListener {
                    Toast.makeText(requireContext(), "Şehir ve geziler silindi", Toast.LENGTH_LONG)
                        .show()
                    val action = SehirDetayFragmentDirections.actionSehirDetayFragmentToSeyahatlerimFragment()
                    val navOptions = NavOptions.Builder().setPopUpTo(R.id.nav_graph, true).build() // geri dönmesin
                    Navigation.findNavController(view).navigate(action,navOptions)


                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Şehir silinemedi", Toast.LENGTH_LONG).show()
                }

            }.addOnFailureListener {
                Toast.makeText(requireContext(),"Şehir bulunamadı..", Toast.LENGTH_LONG).show()
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}