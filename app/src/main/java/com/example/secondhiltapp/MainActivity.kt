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
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.secondhiltapp.data.SoccerVideos
import com.example.secondhiltapp.databinding.ActivityMainBinding
import com.example.secondhiltapp.db.entity.BookMarkData
import com.example.secondhiltapp.db.entity.LanguageData
import com.example.secondhiltapp.preferences.SortOrder
import com.example.secondhiltapp.ui.bookmarks.BookmarkFragment
import com.example.secondhiltapp.ui.details.DetailsFragment
import com.example.secondhiltapp.ui.details.NewsDetailsFragment
import com.example.secondhiltapp.ui.gallery.GalleryFragment
import com.example.secondhiltapp.ui.home.HomeFragment
import com.example.secondhiltapp.ui.stats.StatsFragment
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

const val LOCAL_ENGLISH = "en"
const val LOCAL_CHINESE = "zh"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    GalleryFragment.OnItemClicked,
    BookmarkFragment.NavigateToFragment {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var local: Locale
    lateinit var coordinatedLayout: CoordinatorLayout
    private lateinit var appBarLayout: AppBarLayout

    private lateinit var homeFragment: Fragment
    private lateinit var galleryFragment: GalleryFragment
    private lateinit var statsFragment: StatsFragment
    private lateinit var bookmarkFragment: BookmarkFragment

    private lateinit var newsDetailsFragment: NewsDetailsFragment
    private lateinit var detailsFragment: DetailsFragment

    private val fragments: Array<Fragment>
        get() = arrayOf(
            homeFragment,
            galleryFragment,
            statsFragment,
            bookmarkFragment
        )

    private var selectedIndex = 0

    private val selectedFragment get() = fragments[selectedIndex]

    private fun selectFragment(selectedFragment: Fragment) {
        var transaction = supportFragmentManager.beginTransaction()
        fragments.forEachIndexed { index, fragment ->
            if (selectedFragment == fragment) {
                transaction = transaction.attach(fragment)
                selectedIndex = index
            } else {
                transaction = transaction.detach(fragment)
            }
        }
        transaction.commit()

        title = when (selectedFragment) {
            is HomeFragment -> "HOME"//getString(R.string.title_breaking_news)
            is GalleryFragment -> "HIGHLIGHTS"
            is StatsFragment -> "STATS"
            is BookmarkFragment -> "BOOKMARKS"
            else -> ""
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLanguage()

        binding.apply {
            coordinatedLayout = mainCoordinatedLayout
            appBarLayout = appBar
        }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.findNavController()

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.galleryFragment,
                R.id.statsFragment,
                R.id.bookmarkFragment
            )
        )

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)

        if (savedInstanceState == null) {
            homeFragment = HomeFragment()
            galleryFragment = GalleryFragment(this)
            statsFragment = StatsFragment()
            bookmarkFragment = BookmarkFragment(this)

            supportFragmentManager.beginTransaction()
                .add(R.id.nav_host_fragment_activity_main, homeFragment, TAG_HOME_FRAGMENT)
                .add(R.id.nav_host_fragment_activity_main, galleryFragment, TAG_GALLERY_FRAGMENT)
                .add(R.id.nav_host_fragment_activity_main, statsFragment, TAG_STATS_FRAGMENT)
                .add(R.id.nav_host_fragment_activity_main, bookmarkFragment, TAG_BOOKMARKS_FRAGMENT)
                .commit()
        } else {
            homeFragment =
                supportFragmentManager.findFragmentByTag(TAG_HOME_FRAGMENT) as HomeFragment
            galleryFragment =
                supportFragmentManager.findFragmentByTag(TAG_GALLERY_FRAGMENT) as GalleryFragment
            statsFragment =
                supportFragmentManager.findFragmentByTag(TAG_STATS_FRAGMENT) as StatsFragment
            bookmarkFragment =
                supportFragmentManager.findFragmentByTag(TAG_BOOKMARKS_FRAGMENT) as BookmarkFragment

            selectedIndex = savedInstanceState.getInt(KEY_SELECTED_INDEX, 0)
        }

        selectFragment(selectedFragment)

        binding.bottomNav.setOnItemSelectedListener { item ->
            appBarLayout.setExpanded(true, true)
            val fragment = when (item.itemId) {
                R.id.homeFragment -> homeFragment
                R.id.galleryFragment -> galleryFragment
                R.id.statsFragment -> statsFragment
                R.id.bookmarkFragment -> bookmarkFragment
                else -> throw IllegalArgumentException("Unexpected itemId")
            }

            if (selectedFragment === fragment) {
                if (fragment is OnBottomNavigationFragmentReselectedListener) {
                    fragment.onBottomNavigationFragmentReselected()
                }
            } else {
                selectFragment(fragment)
            }
            true
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_INDEX, selectedIndex)
    }

    interface OnBottomNavigationFragmentReselectedListener {
        fun onBottomNavigationFragmentReselected()
    }

    private fun setupLanguage() {
        mainViewModel.currentLang().observe(this@MainActivity) {
            if (it != null) {
                setLocale(it.language)
            }
        }
    }

    override fun onBackPressed() {
        appBarLayout.setExpanded(true, true)
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        appBarLayout.setExpanded(true, true)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item!!.itemId
        if (id == R.id.action_english) {
            mainViewModel.setLanguage(LanguageData(LOCAL_ENGLISH))
        }
        if (id == R.id.action_chinese) {
            mainViewModel.setLanguage(LanguageData(LOCAL_CHINESE))
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

    private fun setLocalLanguage(
        dm: DisplayMetrics,
        conf: Configuration,
        res: Resources,
        language: String
    ) {
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

    override fun onVideoClick(soccerVideos: SoccerVideos) {
        navigateToDetailsFragment(soccerVideos.video!!)
    }

    private fun navigateToDetailsFragment(soccerVideos: String) {
        appBarLayout.setExpanded(true, true)
        detailsFragment = DetailsFragment(soccerVideos)
        setupFragment(detailsFragment, TAG_DETAILS_FRAGMENT,"DETAILS")
    }

    private fun navigateToNewsFragment(bookmark: BookMarkData, l: String) {
        appBarLayout.setExpanded(true, true)
        newsDetailsFragment = NewsDetailsFragment(bookmark, l)
        setupFragment(newsDetailsFragment, TAG_NEWS_FRAGMENT,"NEWS")
    }

    private fun setupFragment(fragment: Fragment, tag: String, name: String){
        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment_activity_main, fragment, tag)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }

    override fun bookmarkNavigateTo(bookmark: BookMarkData, lang: String) {
        when (bookmark.type) {
            SortOrder.BY_NEWS -> {
                navigateToNewsFragment(bookmark,lang)
            }
            SortOrder.BY_HIGHLIGHTS -> {
                navigateToDetailsFragment(bookmark.video!!)
            }
        }
    }


}

private const val TAG_HOME_FRAGMENT = "TAG_HOME_FRAGMENT"
private const val TAG_GALLERY_FRAGMENT = "TAG_GALLERY_FRAGMENT"
private const val TAG_STATS_FRAGMENT = "TAG_STATS_FRAGMENT"
private const val TAG_BOOKMARKS_FRAGMENT = "TAG_BOOKMARKS_FRAGMENT"
private const val TAG_DETAILS_FRAGMENT = "TAG_DETAILS_FRAGMENT"
private const val TAG_NEWS_FRAGMENT = "TAG_NEWS_FRAGMENT"
private const val KEY_SELECTED_INDEX = "KEY_SELECTED_INDEX"

//        val darkModeFlag = AppCompatDelegate.MODE_NIGHT_YES
//
//        AppCompatDelegate.setDefaultNightMode(darkModeFlag)