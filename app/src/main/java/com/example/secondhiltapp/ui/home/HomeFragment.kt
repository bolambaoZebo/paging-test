package com.example.secondhiltapp.ui.home

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.secondhiltapp.MainActivity
import com.example.secondhiltapp.R
import com.example.secondhiltapp.databinding.HomeFragmentBinding
import com.example.secondhiltapp.db.entity.SoccerNews
import com.example.secondhiltapp.ui.home.adapter.HomeSliderAdapter
import com.example.secondhiltapp.utils.Resource
import com.example.secondhiltapp.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment),
    HomeFragmentAdapter.OnClickListeners,
    HomeSliderAdapter.OnItemSliderClick,
    MainActivity.OnBottomNavigationFragmentReselectedListener{
    private val viewModel by viewModels<HomeViewModel>()
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding

//    private val homeAdapter: HomeFragmentAdapter by lazy {
//        HomeFragmentAdapter(
//            requireContext(),
//            this
//        )
//    }
    lateinit var homRecyclerView: RecyclerView
    lateinit var refreshLayout: SwipeRefreshLayout

    private lateinit var sliderAdapter: HomeSliderAdapter
    private lateinit var homeViewPager2: ViewPager2

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
            onItemClick = { soccer ->
            },
            onBookmarkClick = { soccer ->
                viewModel.onSaveNews(soccer)
            },
            onLikeClick = { soccer ->
                Toast.makeText(requireContext(), "like", Toast.LENGTH_SHORT).show()
            },
            onCommentClick = { soccer ->
                Toast.makeText(requireContext(), "comment", Toast.LENGTH_SHORT).show()
            }
        )

        homeAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        sliderPopulate()

        dots = arrayOfNulls<TextView>(viewModel.sliderImageUrl.size)
        sliderAdapter = HomeSliderAdapter(this)

        binding?.apply {
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
                        homePagerDots.isVisible = !result.data.isNullOrEmpty() //is Resource.Success
                        homeViewPager.isVisible = !result.data.isNullOrEmpty() //is Resource.Success
                        textViewError.isVisible = result.error != null && result.data.isNullOrEmpty()
                        buttonRetry.isVisible = result.error != null && result.data.isNullOrEmpty()
                    }
                    homeAdapter.submitList(result.data){
                        homeAdapter.notifyDataSetChanged()
                    }
                    result.data?.let { it1 -> sliderAdapter.setupList(it1) }

                }
            }

            viewModel.currentLang().observe(viewLifecycleOwner) {
                if (it != null) {
                    homeAdapter.setLanguagesTo(it.language)
                }
            }

            refreshLayout.setOnRefreshListener {
                viewModel.onManualRefresh()
            }


        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect { event ->
                when (event) {
                    is HomeViewModel.AddEditTaskEvent.SaveBookmark -> {
                        requireActivity().snackBar(event.msg, requireActivity())
                    }
                    is HomeViewModel.AddEditTaskEvent.AlreadySaved -> {
                        requireActivity().snackBar(event.msg, requireActivity(), false)
                    }
                }
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
                    dots[i]?.text = Html.fromHtml("&#8226", Html.FROM_HTML_MODE_COMPACT)
                } else {
                    dots[i]?.text = Html.fromHtml("&#8226")
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
        viewModel.onSaveNews(data)
    }

    override fun onSliderImageClicked(imageUrl: String) {
        //Toast.makeText(requireContext(), imageUrl, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        homeViewPager2.unregisterOnPageChangeCallback(onImageChangeCallback)
        _binding = null
    }

    override fun onBottomNavigationFragmentReselected() {
        binding?.homeRecyclerView?.scrollToPosition(0)
    }
}