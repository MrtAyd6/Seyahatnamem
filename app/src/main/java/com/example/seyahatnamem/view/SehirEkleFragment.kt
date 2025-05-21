package com.example.seyahatnamem.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import com.example.seyahatnamem.R

import com.example.seyahatnamem.databinding.FragmentSehirEkleBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

val sehirler = listOf(
    "Şehir Seç","Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Aksaray", "Amasya", "Ankara", "Antalya",
    "Ardahan", "Artvin", "Aydın", "Balıkesir", "Bartın", "Batman", "Bayburt", "Bilecik",
    "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale", "Çankırı", "Çorum",
    "Denizli", "Diyarbakır", "Düzce", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir",
    "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Iğdır", "Isparta", "İstanbul",
    "İzmir", "Kahramanmaraş", "Karabük", "Karaman", "Kars", "Kastamonu", "Kayseri", "Kilis",
    "Kırıkkale", "Kırklareli", "Kırşehir", "Kocaeli", "Konya", "Kütahya", "Malatya", "Manisa",
    "Mardin", "Mersin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Osmaniye", "Rize",
    "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas", "Şanlıurfa", "Şırnak", "Tekirdağ",
    "Tokat", "Trabzon", "Tunceli", "Uşak", "Van", "Yalova", "Yozgat","Zonguldak"
)


class SehirEkleFragment : Fragment() {


    private var _binding: FragmentSehirEkleBinding? = null
    private val binding get() = _binding!!
    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
        auth = Firebase.auth
        //val db = FirebaseFirestore.getInstance(FirebaseFirestoreOptions.Builder().setDatabaseId("seyahatnamem").build())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSehirEkleBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.kaydetButton.setOnClickListener { kaydet(it) }


        val autoComplete = binding.sehirAutoComplete

        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.spinner_dropdown_item,sehirler)

        autoComplete.setAdapter(arrayAdapter)





        autoComplete.setOnTouchListener { view, event ->
            view.performClick() // accessibility için
            autoComplete.showDropDown() // Listeyi aç

            // Klavyeyi açma (odak alıyor ama klavye gelmiyor)
            autoComplete.inputType = InputType.TYPE_NULL

            true // olay buradatüketildi
        }

        autoComplete.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // Kullanıcı ikinci kez tıkladıysa (odak aldıysa)
                autoComplete.inputType = InputType.TYPE_CLASS_TEXT
                }
        }


    }




    private fun kaydet(view: View) {
        val eklenecekSehir = binding.sehirAutoComplete.text.toString()
        val kullaniciEmail = FirebaseAuth.getInstance().currentUser?.email
        val sehirMap = hashMapOf<String, Any>()
        if (eklenecekSehir == "Şehir Seç" || eklenecekSehir.isEmpty()) {
            Toast.makeText(requireContext(), "şehir seçimini kontrol et", Toast.LENGTH_LONG).show()
        } else {
            sehirMap.put("Sehir_adi",eklenecekSehir)
            sehirMap.put("ID",kullaniciEmail!!)


            db.collection("Sehirler").add(sehirMap).addOnSuccessListener { documentReference ->
                //veri database'e yüklendi
                Toast.makeText(requireContext(), "şehir eklendi", Toast.LENGTH_LONG).show()
                val action = SehirEkleFragmentDirections.actionSehirEkleFragmentToSeyahatlerimFragment()
                Navigation.findNavController(view).navigate(action)

            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
            }

        }
    }

            





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}