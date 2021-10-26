package com.example.secondhiltapp.ui.stats

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.webkit.WebSettings
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
//                settings.setAppCacheEnabled(true)
//                settings.cacheMode = WebSettings.LOAD_DEFAULT
//                settings.setAppCachePath(cacheDir.path)
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        binding?.apply {
            scoreWebview.removeAllViews()
            scoreWebview.destroy()
            scoreWebview.clearCache(true)
            scoreWebview.clearHistory()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu!!.findItem(R.id.language_icon).isVisible = false
    }
}