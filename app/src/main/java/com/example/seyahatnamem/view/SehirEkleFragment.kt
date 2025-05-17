package com.example.seyahatnamem.view

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.Navigation
import com.example.seyahatnamem.R

import com.example.seyahatnamem.databinding.FragmentSehirEkleBinding


class SehirEkleFragment : Fragment() {
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

    private var _binding: FragmentSehirEkleBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        //autoComplete.setOnClickListener {
        //    autoComplete.showDropDown()

       // }
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




    private fun kaydet(view: View){
        val action = SehirEkleFragmentDirections.actionSehirEkleFragmentToSeyahatlerimFragment()
        Navigation.findNavController(view).navigate(action)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}