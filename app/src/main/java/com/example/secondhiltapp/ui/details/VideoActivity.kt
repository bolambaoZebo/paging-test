package com.example.secondhiltapp.ui.details

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import com.example.secondhiltapp.R
import com.example.secondhiltapp.databinding.ActivityVideoBinding

private val URL_3WE = "https://asia3we.com/"

class VideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val video = intent.getStringExtra("video")

        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = resources.getString(R.string.app_name)
            setDisplayHomeAsUpEnabled(true)
        }
        binding.apply {

            webview.apply {
                video?.let { this.loadUrl(it) }
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        if (binding != null) {
                            binding.progressBar.isVisible = false
                            binding.imageBanner.apply {
                                setImageResource(R.drawable.banner_one)
                            }
                        }
                    }
                }
            }

            btnClickhere.apply {
                setOnClickListener {
                    val openURL = Intent(Intent.ACTION_VIEW)
                    openURL.data = Uri.parse(URL_3WE)
                    context?.startActivity(openURL, null)
                }
                paint.isUnderlineText = true
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.apply {
            webview.removeAllViews()
            webview.destroy()
            webview.clearCache(true)
            webview.clearHistory()
        }
    }
}