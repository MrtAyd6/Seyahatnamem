package com.beyzanurcelebi.kullanicigiris

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.beyzanurcelebi.kullanicigiris.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.girisButonu.setOnClickListener{ //Giris Yap butonuna tıklandıgında hosgeldiniz sayfasına gitmek icin olusturuldu
            intent = Intent(applicationContext,Hosgeldiniz::class.java)
            startActivity(intent)
        }

        binding.kaydolmaButonu.setOnClickListener { //Kaydol butonuna tıklandığında kaydolma sayfasına geçmek için oluşturuldu.
            intent = Intent(applicationContext,kayitOl::class.java)
            startActivity(intent)

        }


    }

}