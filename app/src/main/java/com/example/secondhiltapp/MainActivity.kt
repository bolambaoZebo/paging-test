package com.example.secondhiltapp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.secondhiltapp.databinding.ActivityMainBinding
import com.example.secondhiltapp.db.entity.LanguageData
import com.example.secondhiltapp.ui.details.ScoreDetailsFragment
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

const val LOCAL_ENGLISH = "en"
const val LOCAL_CHINESE = "zh"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var local: Locale
    lateinit var coordinatedLayout: CoordinatorLayout
    private lateinit var appBarLayout: AppBarLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val darkModeFlag = AppCompatDelegate.MODE_NIGHT_YES
//
//        AppCompatDelegate.setDefaultNightMode(darkModeFlag)

        setupLanguage()

        binding.apply {
            coordinatedLayout = mainCoordinatedLayout
            appBarLayout = appBar
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.findNavController()

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.galleryFragment,
                R.id.bookmarkFragment
            )
        )

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    isNotReselected(item.itemId, navController.currentDestination?.id)//navController.navigate(item.itemId)
                    return@setOnItemSelectedListener true
                }
                R.id.galleryFragment -> {
                    isNotReselected(item.itemId, navController.currentDestination?.id)
                    return@setOnItemSelectedListener true
                }
                R.id.bookmarkFragment -> {
                    isNotReselected(item.itemId, navController.currentDestination?.id)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }

    }

    private fun setupLanguage(){
        mainViewModel.currentLang().observe(this@MainActivity){
            if (it != null){
            setLocale(it.language)
            }
        }
    }

    private fun isNotReselected(id: Int, currentId: Int?) {
        if(id != currentId && currentId != null){
            navController.navigate(id)
            appBarLayout.setExpanded(true,true)
        }
    }

    override fun onBackPressed() {
        appBarLayout.setExpanded(true,true)
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        appBarLayout.setExpanded(true,true)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item!!.itemId
        if (id == R.id.action_english){
           mainViewModel.setLanguage(LanguageData(LOCAL_ENGLISH))
        }
        if (id == R.id.action_chinese){
            mainViewModel.setLanguage(LanguageData(LOCAL_CHINESE))
        }
        if (id == R.id.action_score){
            Intent(this, ScoreDetailsFragment::class.java).apply {
                startActivity(this)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    //change local language method
    private fun setLocale(language: String) {
        local = Locale(language)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration

        val currentLang = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            conf.locales[0]
        } else {
            applicationContext.resources.configuration.locale.country
        }

        val newCurrent = currentLang.toString().split("_")

        if (currentLang.toString() != LOCAL_ENGLISH && currentLang.toString() != LOCAL_CHINESE) {
            if (newCurrent[0] != language) {
                setLocalLanguage(dm, conf, res, language)
            }
        } else {
            if (currentLang.toString() != language) {
                setLocalLanguage(dm, conf, res, language)
            }
        }
    }

    private fun setLocalLanguage(dm: DisplayMetrics, conf: Configuration, res: Resources, language: String) {
        conf.setLocale(local)
        res.updateConfiguration(conf, dm)
        refreshIntent(this, MainActivity::class.java)
        mainViewModel.setLanguage(LanguageData(language))
    }

    private fun refreshIntent(context: Context, java: Class<MainActivity>) {
        val refresh = Intent(context, java)
        startActivity(refresh)
        finish()
    }


}