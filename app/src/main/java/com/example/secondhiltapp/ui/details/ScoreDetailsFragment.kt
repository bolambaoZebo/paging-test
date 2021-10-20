package com.example.secondhiltapp.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.example.secondhiltapp.R
import com.example.secondhiltapp.databinding.ActivityMainBinding
import com.example.secondhiltapp.databinding.ActivityScoreDetailsFragmentBinding
import kotlinx.android.synthetic.main.activity_score_details_fragment.*

class ScoreDetailsFragment : AppCompatActivity() {

    private lateinit var binding: ActivityScoreDetailsFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreDetailsFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            scoreWebview.apply {
                this.loadUrl("https://www.scorebat.com/embed/livescore/")
                settings.javaScriptEnabled = true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.apply {
            scoreWebview.removeAllViews()
            scoreWebview.destroy()
            scoreWebview.clearCache(true)
            scoreWebview.clearHistory()
        }
    }
}