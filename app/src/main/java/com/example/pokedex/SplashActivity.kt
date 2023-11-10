package com.example.pokedex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SplashActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()

        super.onCreate(savedInstanceState)

         setContentView(R.layout.activity_splash)
         screenSplash.setKeepOnScreenCondition{false}

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() //mata esta pantalla
    }
}