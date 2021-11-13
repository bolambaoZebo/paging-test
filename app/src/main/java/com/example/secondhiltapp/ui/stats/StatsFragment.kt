package com.example.secondhiltapp.ui.stats

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.secondhiltapp.R
import com.example.secondhiltapp.databinding.StatsFragmentBinding
import okhttp3.internal.cacheGet

class StatsFragment : Fragment(R.layout.stats_fragment) {

    private var _binding: StatsFragmentBinding? = null
    private val binding get() = _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = StatsFragmentBinding.bind(view)
        binding?.apply {
            scoreWebview.apply {
                this.loadUrl("https://www.scorebat.com/embed/livescore/")
                settings.javaScriptEnabled = true
                settings.setAppCacheEnabled(true)
                settings.cacheMode = WebSettings.LOAD_DEFAULT
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.language_icon).isVisible = false
    }
}

//        if (savedInstanceState == null) {
//            webView.apply {
//                this.loadUrl("https://www.scorebat.com/embed/livescore/")
//                settings.javaScriptEnabled = true
//                settings.setAppCacheEnabled(true)
//                settings.cacheMode = WebSettings.LOAD_DEFAULT
//            }
//
//        }else {
//            webView.restoreState(webState)
//        }

//        binding?.apply {
//            scoreWebview.removeAllViews()
//            scoreWebview.destroy()
//            scoreWebview.clearCache(true)
//            scoreWebview.clearHistory()
//        }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        webView.saveState(outState)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        webState = Bundle()
//        webView.saveState(webState)
//    }

//            scoreWebview.apply {
//                this.loadUrl("https://www.scorebat.com/embed/livescore/")
//                settings.javaScriptEnabled = true
//                settings.setAppCacheEnabled(true)
//                settings.cacheMode = WebSettings.LOAD_DEFAULT
////                settings.setAppCacheEnabled(true)
////                settings.cacheMode = WebSettings.LOAD_DEFAULT
////                settings.setAppCachePath(cacheDir.path)
//            }