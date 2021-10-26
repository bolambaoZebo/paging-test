package com.example.secondhiltapp.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.secondhiltapp.R
import com.example.secondhiltapp.data.SoccerVideos
import com.example.secondhiltapp.databinding.FragmentDetailsBinding
import com.example.secondhiltapp.databinding.FragmentGalleryBinding
import com.example.secondhiltapp.ui.gallery.GalleryFragment

private val URL_3WE = "https://asia3we.com/"

class DetailsFragment(
    private val url: String
) : Fragment(R.layout.fragment_details) {

    private val args by navArgs<DetailsFragmentArgs>()

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDetailsBinding.bind(view)

        binding.apply {

            val video = url//soccerVideos.video//args.video
            activity?.let {
            }
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

        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu!!.findItem(R.id.language_icon).isVisible = false
//        menu!!.findItem(R.id.action_score).isVisible = false

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


//            Glide.with(this@DetailsFragment)
//.load(R.drawable.banner_one)
//.error(R.drawable.ic_error)
//.listener( object : RequestListener<Drawable>{
//    override fun onLoadFailed(
//        e: GlideException?,
//        model: Any?,
//        target: Target<Drawable>?,
//        isFirstResource: Boolean
//    ): Boolean {
//        progressBar.isVisible = false
//        return false
//    }
//
//    override fun onResourceReady(
//        resource: Drawable?,
//        model: Any?,
//        target: Target<Drawable>?,
//        dataSource: DataSource?,
//        isFirstResource: Boolean
//    ): Boolean {
//        progressBar.isVisible = false
//        textViewCreator.isVisible = true
//        textViewDescription.isVisible = video.title != null
//        return false
//    }
//
//})
//.load(image_banner)


//
//dialogBind.webview.apply {
//    this.loadUrl(videos)
//    settings.javaScriptEnabled = true
//}
//
//dialogBind.btnClickhere.setOnClickListener{
//    toThreeLink(context)
//}
//
//dialogBind.imgExit.setOnClickListener {
//    dialogBind.webview.removeAllViews()
//    dialogBind.webview.destroy()
//    dialogBind.webview.clearCache(true)
//    dialogBind.webview.clearHistory()
//    dialog.dismiss()
//}