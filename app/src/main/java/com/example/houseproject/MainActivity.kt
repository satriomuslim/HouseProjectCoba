package com.example.houseproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.houseproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.myButton.setOnClickListener {
            val stickyBottomSheet: BottomSheet2Fragment = BottomSheet2Fragment.newInstance()
            if (!stickyBottomSheet.isAdded) stickyBottomSheet.show(
                supportFragmentManager,
                "StickyBottomSheet"
            )
        }

        binding.myButton2.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }

    }
}