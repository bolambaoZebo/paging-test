package com.example.secondhiltapp.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.secondhiltapp.*
import com.example.secondhiltapp.databinding.HomeFragmentBinding
import com.example.secondhiltapp.db.entity.AdsModel
import com.example.secondhiltapp.db.entity.SoccerNews
import com.example.secondhiltapp.ui.details.NewsActivity
import com.example.secondhiltapp.ui.home.adapter.HomeSliderAdapter
import com.example.secondhiltapp.utils.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import java.lang.Exception

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment),
    HomeFragmentAdapter.OnClickListeners,
    HomeSliderAdapter.OnItemSliderClick {

    private val viewModel by viewModels<HomeViewModel>()
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding

    lateinit var homRecyclerView: RecyclerView
    lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var sliderAdapter: HomeSliderAdapter
    private lateinit var homeViewPager2: ViewPager2
    private lateinit var scrollUp: FloatingActionButton
    private lateinit var dots: Array<TextView?>

    private var onImageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            addDotView(position)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = HomeFragmentBinding.bind(view)

        val homeAdapter = HomeAdapter(
            onItemClick = { soccer, lang ->
                val tNews = if (lang == LOCAL_ENGLISH) soccer.title!! else soccer.titleChinese!!
                val tDes = if (lang == LOCAL_ENGLISH) soccer.description!! else soccer.descriptionChinese!!
                Intent(requireContext(), NewsActivity::class.java).apply {
                    this.putExtra(IMAGE_STRING, soccer.imageUrl)
                    this.putExtra(TITLE_STRING, tNews)
                    this.putExtra(DESCRIPTION_STRING, tDes)
                    startActivity(this)
                }
            },
            onBookmarkClick = { soccer, lang ->
                viewModel.onSaveNews(soccer,requireContext(), lang)
            },
            onLikeClick = { soccer ->
                Toast.makeText(requireContext(), "like", Toast.LENGTH_SHORT).show()
            },
            onCommentClick = { soccer ->
                Toast.makeText(requireContext(), "comment", Toast.LENGTH_SHORT).show()
            },
            context = requireContext()
        )

        homeAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        sliderPopulate()

        dots = arrayOfNulls<TextView>(viewModel.sliderImageUrl.size)
        sliderAdapter = HomeSliderAdapter(this)

        binding?.apply {
            scrollUp = homeArrowFab
            homRecyclerView = homeRecyclerView
            refreshLayout = swipeRefreshLayout
            homeViewPager2 = homeViewPager
            homRecyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = homeAdapter
            }

            homeViewPager2.apply {
                adapter = sliderAdapter
                registerOnPageChangeCallback(onImageChangeCallback)
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.soccerData.collect {
                    val result = it ?: return@collect

                    refreshLayout.isRefreshing = result is Resource.Loading
                    binding?.apply {
                        homePagerDots.isVisible =
                            !result.data.isNullOrEmpty()// && result is Resource.Success
                        homeViewPager.isVisible =
                            !result.data.isNullOrEmpty()// && result is Resource.Success
                        textViewError.isVisible =
                            result.error != null && result.data.isNullOrEmpty()
                        buttonRetry.isVisible = result.error != null && result.data.isNullOrEmpty()
                    }

                    homeAdapter.submitList(result.data) {
                        homeAdapter.notifyDataSetChanged()
                    }

                    sliderAdapter.setupList(viewModel.sliderImageUrl)
                }
            }

        }

        refreshLayout.setOnRefreshListener {
            viewModel.onManualRefresh()
        }

        button_retry.setSafeOnClickListener {
            viewModel.onManualRefresh()
        }

        viewModel.currentLang().observe(viewLifecycleOwner) {
            if (it != null) {
                homeAdapter.setLanguagesTo(it.language)
            }
        }

        scrollUp.setSafeOnClickListener {
            homRecyclerView.smoothScrollToPosition(0)
            scrollUp.hide()
        }


        homeRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                viewModel.isScrolling.value = newState == RecyclerView.SCROLL_STATE_IDLE
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy > 0) scrollUp.show() else scrollUp.hide()

                viewModel.isScrolling.observe(viewLifecycleOwner) {
                    if (!scrollUp.isVisible && it ) {
                        binding?.homeViewPager?.isVisible = true
                        binding?.homePagerDots?.isVisible = true
                    } else {
                        binding?.homeViewPager?.isVisible = false
                        binding?.homePagerDots?.isVisible = false
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect { event ->
                when (event) {
                    is HomeViewModel.AddEditTaskEvent.SaveBookmark -> {
                        requireActivity().snackBar(event.msg, requireActivity())
                    }
                    is HomeViewModel.AddEditTaskEvent.AlreadySaved -> {
                        requireActivity().snackBar(event.msg, requireActivity(), false)
                    }
                    else -> ""
                }.exhaustive
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    private fun sliderPopulate() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            while (true) {
                for (i in 0 until viewModel.sliderImageUrl.size) {
                    delay(2500)
                    if (i == 0) {
                        homeViewPager2.setCurrentItem(i, false)
                    } else {
                        homeViewPager2.setCurrentItem(i, true)
                    }
                }
            }
        }
    }

    private fun addDotView(currentPage: Int) {
        binding!!.homePagerDots.removeAllViews()
        if (currentPage <= 3) {
            for (i in viewModel.sliderImageUrl.indices) {
                dots[i] = TextView(requireContext())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dots[i]?.text = Html.fromHtml(FROM_HTML, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    dots[i]?.text = Html.fromHtml(FROM_HTML)
                }

                dots[i]?.textSize = 38f
                dots[i]?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding!!.homePagerDots.addView(dots[i])
            }
            if (dots.isNotEmpty()) {
                dots[currentPage]?.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.purple_700
                    )
                )
            }
        }

    }

    override fun onSaveClick(data: SoccerNews) {
//        viewModel.onSaveNews(data, requireContext(), lang)
    }

    override fun onSliderImageClicked(imageUrl: String) {
        viewModel.isActive?.observe(viewLifecycleOwner) {
            if (it != null && it.isActive == true) {
                requireContext().goTo3WE()
            } else {
                requireActivity().snackBar(
                    resources.getString(R.string.thank_you_for_visiting),
                    requireActivity()
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        homeViewPager2.unregisterOnPageChangeCallback(onImageChangeCallback)
        _binding = null
    }

}

private const val FROM_HTML = "&#8226"
