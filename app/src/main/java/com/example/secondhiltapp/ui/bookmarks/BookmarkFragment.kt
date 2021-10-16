package com.example.secondhiltapp.ui.bookmarks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.secondhiltapp.R
import com.example.secondhiltapp.databinding.BookmarkFragmentBinding
import com.example.secondhiltapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment(R.layout.bookmark_fragment) {

    private val viewModel by viewModels<BookmarkViewModel>()

    private var _binding: BookmarkFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = BookmarkFragmentBinding.bind(view)

        val bookmarkAdapter = BookmarkAdapter()

        binding.apply {
            bookmarkRecyclerView.apply {
                adapter = bookmarkAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = bookmarkAdapter.currentList[viewHolder.adapterPosition]
                    // viewModel.onTaskSwiped(task)
                }
            }).attachToRecyclerView(bookmarkRecyclerView)

        }

        viewModel.bookmark.observe(viewLifecycleOwner) {
            bookmarkAdapter.submitList(it)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_bookmarks, menu)
        menu!!.findItem(R.id.language_icon).isVisible = false
        menu!!.findItem(R.id.sorting).isVisible = false
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}