package com.example.secondhiltapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val time: Long = 2000

        Handler().postDelayed({
            Intent(this@SplashScreen, MainActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }, time)

    }
}