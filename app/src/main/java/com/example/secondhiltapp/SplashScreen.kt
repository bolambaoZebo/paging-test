package com.example.secondhiltapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val time: Long = 1800
        lifecycleScope.launchWhenStarted {
            delay(time)
            withContext(Dispatchers.Main){
                Intent(this@SplashScreen, MainActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }
        }

    }
}