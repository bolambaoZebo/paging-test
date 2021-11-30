package com.example.secondhiltapp.ui.details

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.secondhiltapp.R
import com.example.secondhiltapp.databinding.ActivityNewsBinding
import com.example.secondhiltapp.databinding.PopupDialogBinding
import com.example.secondhiltapp.utils.*
import java.util.*

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

        popupAds(this@NewsActivity)

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
    private fun popupAds(context: Context) {
        val url = URL_3WE
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(url)
        val random = Random()
        val imgs = getResources().obtainTypedArray(R.array.pop_random_);

        val binding: PopupDialogBinding = PopupDialogBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )
        val dialog = Dialog(context)
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        binding.apply {
            background.background = resources.getDrawable(imgs.getResourceId(random.nextInt(3), -1))

            imgExit.setSafeOnClickListener {
                dialog.dismiss()
            }

            btnClickhere.setSafeOnClickListener {
                startActivity(openURL)
            }
        }

        dialog.show();
    }
}