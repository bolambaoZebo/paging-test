package com.example.secondhiltapp.ui.home

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.secondhiltapp.LOCAL_ENGLISH
import com.example.secondhiltapp.R
import com.example.secondhiltapp.databinding.HomeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.delay as delay

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment) {

    private val viewModel by viewModels<HomeViewModel>()

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding

    private val homeAdapter: HomeFragmentAdapter by lazy { HomeFragmentAdapter(requireContext()) }
    lateinit var homeRecyclerView: RecyclerView
    lateinit var refreshLayout: SwipeRefreshLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = HomeFragmentBinding.bind(view)
        homeRecyclerView = binding!!.homeRecyclerView
        refreshLayout = binding!!.swipeRefreshLayout


        refreshLayout.setOnRefreshListener {
            refreshLayout.isRefreshing = false
        }

        initialViewSetup()

        populateView()

    }

    private fun populateView() {
        viewModel.soccerData.observe(viewLifecycleOwner) {
            it.data?.let { it1 ->
                refreshLayout.isRefreshing = false
                homeAdapter.setHomeData(it1)
            }
        }

        viewModel.currentLang().observe(viewLifecycleOwner) {
            if (it != null){
                homeAdapter.setLanguagesTo(it.language)
            }

        }
    }

    private fun initialViewSetup(){
        homeRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = homeAdapter
        }
        refreshLayout.isRefreshing = true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}