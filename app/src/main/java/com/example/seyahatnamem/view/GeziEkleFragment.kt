package com.example.seyahatnamem.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import com.example.seyahatnamem.databinding.FragmentGeziEkleBinding
import com.example.seyahatnamem.model.Sehir
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import kotlin.math.log

class GeziEkleFragment : Fragment() {
    private var _binding: FragmentGeziEkleBinding? = null
    private val binding get() = _binding!!
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var auth : FirebaseAuth
    private lateinit var secilenSehir : Sehir   //sehir listesinden gelen argüman
    private lateinit var db : FirebaseFirestore
    private lateinit var documentID : String    //databasedeki document ID'si

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storage = Firebase.storage
        auth = Firebase.auth
        db = Firebase.firestore
        registerLaunchers()
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
        binding.resimEkle.setOnClickListener { gorselSec(it) }

    }

    private fun kaydet(view: View){
        arguments?.let {
            secilenSehir = GeziEkleFragmentArgs.fromBundle(it).secilenSehir


            //sehir bilgileri kaydedilecek
            gonderiOlustur(view)
        }
    }


    fun gorselSec(view: View){
        val intentToGalery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncher.launch(intentToGalery)
    }

    private fun registerLaunchers() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        secilenGorsel = intentFromResult.data
                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                val source = ImageDecoder.createSource(
                                    requireActivity().contentResolver,
                                    secilenGorsel!!
                                )
                                secilenBitmap = ImageDecoder.decodeBitmap(source)
                                binding.resimEkle.setImageBitmap(secilenBitmap)
                            } else {
                                secilenBitmap = MediaStore.Images.Media.getBitmap(
                                    requireActivity().contentResolver,
                                    secilenGorsel
                                )
                                binding.resimEkle.setImageBitmap(secilenBitmap)
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
    }



    private fun gonderiOlustur(view:View) {
        val targetEmail = auth.currentUser?.email.toString()
        val uuid = UUID.randomUUID()  //random string oluşturur
        val gorselAdi = "${uuid}.jpg"

        val reference = storage.reference
        val gorselReferansi = reference.child("images").child(gorselAdi)

        if (secilenGorsel != null) {

            gorselReferansi.putFile(secilenGorsel!!).addOnSuccessListener { upluadTask ->
                //urlyi alma işlemi
                gorselReferansi.downloadUrl.addOnSuccessListener { uri ->
                    if (auth.currentUser != null) {
                        val downloadUrl = uri.toString()
                        //veritabanına kayıt yapılacak

                        val geziMap = hashMapOf<String, Any>()
                        val yorum = binding.yorumEkle.text.toString()
                        geziMap.put("gorselUrl", downloadUrl)

                        geziMap.put("yorum", yorum)


                        //BURDAYZ 19MAYIS
                        db.collection("Sehirler").whereEqualTo("ID", targetEmail).whereEqualTo("Sehir_adi", secilenSehir.sehirAdi).get().addOnSuccessListener { documents ->

                            for (document in documents) {
                                documentID = document.id
                            }

                            val sehirRef = db.collection("Sehirler").document(documentID)
                            sehirRef.collection("geziler").add(geziMap).addOnSuccessListener {
                                Toast.makeText(requireContext(), "Gezi eklendi", Toast.LENGTH_LONG).show()

                                val action = GeziEkleFragmentDirections.actionGeziEkleFragmentToSehirDetayFragment(secilenSehir)
                                Navigation.findNavController(view).navigate(action)

                            }.addOnFailureListener {
                                Toast.makeText(requireContext(), "Gezi eklenemedi!!!", Toast.LENGTH_LONG).show()
                            }
                        }
                    }


                }


            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}