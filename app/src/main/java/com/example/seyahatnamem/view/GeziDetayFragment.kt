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
import com.example.seyahatnamem.R
import com.example.seyahatnamem.databinding.FragmentGeziDetayBinding
import com.example.seyahatnamem.model.Gezi
import com.example.seyahatnamem.model.Sehir
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.squareup.picasso.Picasso


class GeziDetayFragment : Fragment() {
    private var _binding: FragmentGeziDetayBinding? = null
    private val binding get() = _binding!!
    private lateinit var secilenGezi : Gezi
    private lateinit var db : FirebaseFirestore
    private lateinit var secilenSehir: Sehir
    private lateinit var sehirDocumentID : String
    private lateinit var geziDocumentID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
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
        binding.guncelleButton.visibility = View.GONE
        arguments?.let{
            secilenGezi = GeziDetayFragmentArgs.fromBundle(it).geziListe
            secilenSehir = GeziDetayFragmentArgs.fromBundle(it).secilenSehir
        }

        Picasso.get().load(secilenGezi.gorselUrl).into(binding.geziFoto)
        binding.geziYorum.setText(secilenGezi.yorum)
        binding.geziAdi.text = secilenGezi.geziAdi

        binding.geziSilButton.setOnClickListener { geziSil(it) }
        binding.guncelleButton.setOnClickListener { yorumGuncelle(it) }
        binding.geziYorum.setOnTouchListener {_,_->
            binding.guncelleButton.visibility = View.VISIBLE
            false
        }



    }

    private fun geziSil(view: View){
        val targetEmail = FirebaseAuth.getInstance().currentUser?.email


        db.collection("Sehirler").whereEqualTo("ID", targetEmail)
            .whereEqualTo("Sehir_adi", secilenSehir.sehirAdi).get()
            .addOnSuccessListener { sehirDocuments ->

                for (document in sehirDocuments) {
                    sehirDocumentID = document.id
                }

                db.collection("Sehirler").document(sehirDocumentID).collection("geziler").whereEqualTo("geziAdi",secilenGezi.geziAdi).get().addOnSuccessListener { geziDocuments ->
                    for (document in geziDocuments){
                        geziDocumentID = document.id
                    }

                    db.collection("Sehirler").document(sehirDocumentID).collection("geziler").document(geziDocumentID).delete().addOnSuccessListener {
                        Toast.makeText(requireContext(),"Gezi bilgileri silindi", Toast.LENGTH_LONG).show()

                        val action = GeziDetayFragmentDirections.actionGeziDetayFragmentToSehirDetayFragment(secilenSehir)
                        val navOptions = NavOptions.Builder().setPopUpTo(R.id.sehirDetayFragment, true).build() // geri dönmesin
                        Navigation.findNavController(view).navigate(action,navOptions)

                    }.addOnFailureListener {
                        Toast.makeText(requireContext(),"Gezi bilgileri silinemedi!!", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(requireContext(),"Gezi bulunamadı!!", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(),"Şehir bulunamadı..", Toast.LENGTH_LONG).show()
            }
    }

    private fun yorumGuncelle(view: View){

        val yeni_yorum = binding.geziYorum.text.toString()
        val targetEmail = FirebaseAuth.getInstance().currentUser?.email

        db.collection("Sehirler").whereEqualTo("ID", targetEmail)
            .whereEqualTo("Sehir_adi", secilenSehir.sehirAdi).get()
            .addOnSuccessListener { sehirDocuments ->

                for (document in sehirDocuments) {
                    sehirDocumentID = document.id
                }

                db.collection("Sehirler").document(sehirDocumentID).collection("geziler").whereEqualTo("geziAdi",secilenGezi.geziAdi).get().addOnSuccessListener { geziDocuments ->
                    for (document in geziDocuments){
                        geziDocumentID = document.id
                    }

                    db.collection("Sehirler").document(sehirDocumentID).collection("geziler").document(geziDocumentID).update("yorum",yeni_yorum).addOnSuccessListener {

                        Toast.makeText(requireContext(),"Yorum güncellendi", Toast.LENGTH_LONG).show()
                        binding.guncelleButton.visibility = View.INVISIBLE

                    }.addOnFailureListener {
                        Toast.makeText(requireContext(),"Yorum güncellenemedi!!", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(requireContext(),"Gezi bulunamadı!!", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(),"Şehir bulunamadı..", Toast.LENGTH_LONG).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}