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
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.seyahatnamem.R
import com.example.seyahatnamem.databinding.FragmentHosgeldinizBinding
import com.example.seyahatnamem.model.Gezi
import com.example.seyahatnamem.model.Kullanici
import com.example.seyahatnamem.model.Sehir
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.util.UUID

class HosgeldinizFragment : Fragment() {
    private var _binding: FragmentHosgeldinizBinding? = null
    private val binding get() = _binding!!
    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var storage: FirebaseStorage
    private lateinit var db : FirebaseFirestore
    private lateinit var documentID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        registerLaunchers()
        storage = Firebase.storage
        db = Firebase.firestore
        firestoreVerileriAl()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHosgeldinizBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cikisYapButonu.setOnClickListener{
            cikisYap(it) //// Çıkış Yap butonuna bastığında ana sayfaya yönlendirmesi için buton oluşturuldu

        }

        binding.profilResmi.setOnClickListener {
            profilResmiEkle(it) //Profil fotoğrına tıklayınca profil fotoğrafı değiştirilir.

        }

        binding.sifreDegistirmeButonu.setOnClickListener {
            sifreDegistir(it)
        }




        val kullaniciEmail = FirebaseAuth.getInstance().currentUser?.email

        binding.kullaniciAdiniGosterenText.text = "E-mail : ${kullaniciEmail}"

        binding.parolayGosterenText.text = "Parola :  ********"



    }





    fun cikisYap(view: View){
        auth.signOut()
        val action = HosgeldinizFragmentDirections.actionHosgeldinizFragmentToGirisFragment()
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_graph, true) // geri dönmesin
            .build()
        Navigation.findNavController(view).navigate(action,navOptions)
    }

    fun profilResmiEkle(view:View){
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
                                binding.profilResmi.setImageBitmap(secilenBitmap)
                                profilResminiVeriTabaninaKaydet(requireView())
                            } else {
                                secilenBitmap = MediaStore.Images.Media.getBitmap(
                                    requireActivity().contentResolver,
                                    secilenGorsel
                                )
                                binding.profilResmi.setImageBitmap(secilenBitmap)
                                profilResminiVeriTabaninaKaydet(requireView())
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

    }



    fun profilResminiVeriTabaninaKaydet(view: View){

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

                        val profilFotoMap = hashMapOf<String, Any>()
                        profilFotoMap.put("ppUrl", downloadUrl)
                        profilFotoMap.put("ID",targetEmail)
                        profilFotoMap.put("sifre","*******")
                        db.collection("KullaniciBilgi").add(profilFotoMap).addOnSuccessListener { documentReference ->
                            //veri database'e yüklendi
                            Toast.makeText(requireContext(), "Profil resmi güncellendi", Toast.LENGTH_SHORT).show()

                        }.addOnFailureListener { exception ->
                            Toast.makeText(requireContext(), "Tekrar deneyiniz", Toast.LENGTH_LONG).show()
                        }
                        }
                    }


                }



        }

    }
    private  fun firestoreVerileriAl(){
        val kullaniciBilgiListesi = ArrayList<Kullanici>()
        val targetEmail = FirebaseAuth.getInstance().currentUser?.email
        db.collection("KullaniciBilgi").whereEqualTo("ID",targetEmail).addSnapshotListener { value, error ->
            if(error != null){
                Toast.makeText(requireContext(),error.localizedMessage, Toast.LENGTH_LONG).show()
            }else{
                println("email :"+targetEmail)
                if(value != null){
                    if(!value.isEmpty){
                        //boş değilse
                        kullaniciBilgiListesi.clear()
                        val documents = value.documents
                        for(document in documents){

                            val ppUrl = document.get("ppUrl") as String

                            val kullanici = Kullanici("${targetEmail}","*****",ppUrl)
                            kullaniciBilgiListesi.add(kullanici)
                        }
                       val pp = Picasso.get().load(kullaniciBilgiListesi[0].profilResmi).into(binding.profilResmi)
                    }
                }
            }
        }
    }

    private fun sifreDegistir(view : View){
        val user = Firebase.auth.currentUser
        val email = user?.email
        val eskiSifre = binding.eskiSifre.text.toString()
        val yeniSifre = binding.yeniSifre.text.toString()

        if(eskiSifre == yeniSifre){
            Toast.makeText(requireContext(),"Ayni sifreyi kullanamazsiniz.",Toast.LENGTH_SHORT).show()
        }else {

            val credential = EmailAuthProvider.getCredential(email!!, eskiSifre) // kullanicinin eski verilerine erisim.

            user.reauthenticate(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Şifre doğrulandı
                    user.updatePassword(yeniSifre).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Toast.makeText(context, "Şifre başarıyla güncellendi", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Şifre güncellenemedi", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Eski şifre hatalı", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}