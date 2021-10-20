package com.example.secondhiltapp.ui.bookmarks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.secondhiltapp.LOCAL_ENGLISH
import com.example.secondhiltapp.R
import com.example.secondhiltapp.databinding.BookmarkFragmentBinding
import com.example.secondhiltapp.db.entity.BookMarkData
import com.example.secondhiltapp.preferences.SortOrder
import com.example.secondhiltapp.utils.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class BookmarkFragment : Fragment(R.layout.bookmark_fragment), BookmarkAdapter.OnItemBookmarkClick {

    private val viewModel by viewModels<BookmarkViewModel>()

    private lateinit var searchView: SearchView

    private var _binding: BookmarkFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = BookmarkFragmentBinding.bind(view)

        val bookmarkAdapter = BookmarkAdapter(this)

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
                    val bookmark = bookmarkAdapter.currentList[viewHolder.adapterPosition]
                     viewModel.onTaskSwiped(bookmark)
                }
            }).attachToRecyclerView(bookmarkRecyclerView)

        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.bookmarkEvent.collect { bookmarkEvent ->
                when(bookmarkEvent){
                    is BookmarkViewModel.BookmarkEvent.ShowUndoDeleteTaskMessage ->{
                        Snackbar.make(requireView(), bookmarkEvent.bookMarkData.title.toString(), Snackbar.LENGTH_LONG)
                            .setAction("UNDO"){
                                viewModel.onUndoDeleteClick(bookmarkEvent.bookMarkData)
                            }.show()
                    }
                    is BookmarkViewModel.BookmarkEvent.NavigateToDeleteAllCompletedScreen -> {
                        val action =
                            BookmarkFragmentDirections.actionGlobalDeleteAllCompletedDialogFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }

        viewModel.bookmark.observe(viewLifecycleOwner) {
            bookmarkAdapter.submitList(it)
        }

        viewModel.currentLang().observe(viewLifecycleOwner) {
            if (it != null){
                bookmarkAdapter.setLanguagesTo(it.language)
            }
        }

        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_bookmarks, menu)
        menu!!.findItem(R.id.language_icon).isVisible = false
        menu!!.findItem(R.id.action_score).isVisible = false

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        val pendingQuery = viewModel.searchQuery.value
        if (pendingQuery != null && pendingQuery.isNotEmpty()){
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }

        searchView.onQueryTextChanged {
          viewModel.searchQuery.value = it
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.action_sort_by_date_created -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                true
            }
            R.id.action_post -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NEWS)
                true
            }
            R.id.action_highlights -> {
                viewModel.onSortOrderSelected(SortOrder.BY_HIGHLIGHTS)
                true
            }
            R.id.action_delete_all_completed_tasks -> {
                viewModel.onDeleteAllCompletedClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(bookmark: BookMarkData, lang: String) {
        viewModel.isActive?.observe(viewLifecycleOwner){
            if (it != null && it.isActive == true){
                when(bookmark.type){
                    SortOrder.BY_NEWS -> {
                        val action =
                            BookmarkFragmentDirections.actionBookmarkFragmentToNewsDetailsFragment(
                                if (lang == LOCAL_ENGLISH)bookmark.title!! else bookmark.titleChinese!!,
                                if (lang == LOCAL_ENGLISH)bookmark.description!! else bookmark.descriptionChinese!!,
                                bookmark.imageUrl!!
                            )
                        findNavController().navigate(action)
                    }
                    SortOrder.BY_HIGHLIGHTS -> {
                        val action =
                            BookmarkFragmentDirections.actionBookmarkFragmentToDetailsFragment(
                                bookmark.video!!
                            )
                        findNavController().navigate(action)
                    }
                }
            }else{
                Snackbar.make(requireView(), resources.getString(R.string.thank_you_for_visiting), Snackbar.LENGTH_LONG)
                    .setAction(resources.getString(R.string.ok)){}
                    .show()
            }

        }

    }
}