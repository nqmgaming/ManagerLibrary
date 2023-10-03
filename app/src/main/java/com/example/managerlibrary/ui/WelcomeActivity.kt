package com.example.managerlibrary.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.managerlibrary.databinding.ActivityWelcomeBinding
import com.example.managerlibrary.ui.account.LoginActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //delay 3 seconds before going to main activity
        val background = object: Thread(){
            override fun run() {
                try {
                    sleep(1500)
                    val intent = Intent(baseContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }

        background.start()
    }
}