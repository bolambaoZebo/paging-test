package com.example.secondhiltapp.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.secondhiltapp.MainActivity
import com.example.secondhiltapp.R
import com.example.secondhiltapp.data.SoccerVideos
import com.example.secondhiltapp.databinding.FragmentGalleryBinding
import com.example.secondhiltapp.db.entity.BookMarkData
import com.example.secondhiltapp.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class GalleryFragment(
    private val listener: OnGalleryVideoClick
) : Fragment(R.layout.fragment_gallery)
{
    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    lateinit var refreshLayout: SwipeRefreshLayout


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleryBinding.bind(view)

        val adapter = SoccerVideoAdapter(
            onBookmarkClick = { video ->
                viewModel.onBookmarkClick(video)
             },
            onItemImage = { video ->
                viewModel.isActive?.observe(viewLifecycleOwner){
                    if (it != null && it.isActive == true){
                        listener.onVideoClick(video)

                    }else{
                        requireActivity().snackBar(resources.getString(R.string.thank_you_for_visiting), requireActivity())
                    }
                }
            }
        )

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = SoccerVideoLoadStateAdapter { adapter.refresh()},
                footer = SoccerVideoLoadStateAdapter { adapter.refresh()}
            )

            refreshLayout = refreshVideo

            buttonRetry.setOnClickListener { adapter.retry()}
            refreshLayout.setOnRefreshListener {
                refreshLayout.isRefreshing = false
                adapter.retry()
            }
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                refreshLayout.isRefreshing = loadState.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                if(loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1){
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                }else{
                    textViewEmpty.isVisible = false
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.photos.observe(viewLifecycleOwner, Observer {

                adapter.submitData(viewLifecycleOwner.lifecycle, it)
            })
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.galleryEvent.collect { video ->
                when(video){
                    is GalleryViewModel.GalleryEvents.SaveHighlights -> {
                       requireActivity().snackBar(video.data, requireActivity())
                    }
                    is GalleryViewModel.GalleryEvents.AllreadySaveHighlights -> {
                        requireActivity().snackBar(video.data, requireActivity(),false)
                    }
                }
            }
        }

        setHasOptionsMenu(true)

    }
//    override fun onItemClicked(video: SoccerVideos) {
//        viewModel.isActive?.observe(viewLifecycleOwner){
//            if (it != null && it.isActive == true){
//                listener.onVideoClick(video)
////                findNavController().navigate(action)
//
//            }else{
//                requireActivity().snackBar(resources.getString(R.string.thank_you_for_visiting), requireActivity())
//            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.language_icon).isVisible = false
    }

    interface OnGalleryVideoClick {
        fun onVideoClick(video: SoccerVideos)
    }

}




