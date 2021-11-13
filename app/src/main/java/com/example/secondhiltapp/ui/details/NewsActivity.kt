package com.example.secondhiltapp.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.secondhiltapp.R
import com.example.secondhiltapp.databinding.ActivityNewsBinding
import com.example.secondhiltapp.utils.DESCRIPTION_STRING
import com.example.secondhiltapp.utils.IMAGE_STRING
import com.example.secondhiltapp.utils.TITLE_STRING

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUrl = intent.getStringExtra(IMAGE_STRING)
        val titleText = intent.getStringExtra(TITLE_STRING)
        val descriptionText = intent.getStringExtra(DESCRIPTION_STRING)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = resources.getString(R.string.app_name)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.apply {
            Glide.with(this@NewsActivity)
                .load(imageUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_baseline_sports_soccer_24)
                .into(newsDetailsImage)

            newsTitle.text = titleText
            newsDescription.text =  descriptionText
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}