package com.example.secondhiltapp.ui.gallery


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.secondhiltapp.R
import com.example.secondhiltapp.data.SoccerVideos
import com.example.secondhiltapp.databinding.FragmentGalleryBinding
import com.example.secondhiltapp.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery), SoccerVideoAdapter.OnItemClickListener{

    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleryBinding.bind(view)

        val adapter = SoccerVideoAdapter(this)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = SoccerVideoLoadStateAdapter { adapter.refresh()},
                footer = SoccerVideoLoadStateAdapter { adapter.refresh()}
            )
            buttonRetry.setOnClickListener {
                adapter.retry()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            viewModel.photos.observe(viewLifecycleOwner, Observer {
                adapter.submitData(viewLifecycleOwner.lifecycle, it)
            })
        }


        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                //empty view
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

        setHasOptionsMenu(true)
    }
    override fun onItemClicked(video: SoccerVideos) {
        val action = GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(video.video!!)
        viewModel.isActive?.observe(viewLifecycleOwner){
            if (it != null && it.isActive == true){
                findNavController().navigate(action)
            }else{
                binding.rootGalleryLayout.snackbar("Thank you for your visit")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu!!.findItem(R.id.language_icon).isVisible = false
        menu!!.findItem(R.id.sorting).isVisible = false
    }
}



//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//
//        inflater.inflate(R.menu.menu_gallery, menu)
//
//        val searchItem = menu.findItem(R.id.action_search)
//        val searchView = searchItem.actionView as SearchView
//
//        searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//
//                if (query != null) {
//                    binding.recyclerView.scrollToPosition(0)
//                    viewModel.searchPhotos(query)
//                    searchView.clearFocus()
//                }
//
//                return true
//
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return true
//            }
//
//        })
//    }



