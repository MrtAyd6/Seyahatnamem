package com.beyzanurcelebi.kullanicigiris

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.beyzanurcelebi.kullanicigiris.databinding.ActivityKayitOlBinding
import com.beyzanurcelebi.kullanicigiris.databinding.ActivityMainBinding

class kayitOl : AppCompatActivity() {
    private lateinit var binding: ActivityKayitOlBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKayitOlBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.giriseDonButonu.setOnClickListener { // Girişe Dön butonuna basıldığında ana sayfaya yönlendirmesi için buton oluşturuldu
            intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
        }


        }


    }
