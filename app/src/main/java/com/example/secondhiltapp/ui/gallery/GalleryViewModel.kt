package com.example.secondhiltapp.ui.gallery

import android.content.Context
import androidx.hilt.Assisted
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.secondhiltapp.R
import com.example.secondhiltapp.data.SoccerRepository
import com.example.secondhiltapp.data.SoccerVideos
import com.example.secondhiltapp.db.BookmarkDao
import com.example.secondhiltapp.db.entity.BookMarkData
import com.example.secondhiltapp.preferences.BOOKMARKTYPE
import com.example.secondhiltapp.preferences.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    repository: SoccerRepository,
    private val bookmarkDao: BookmarkDao,
    @Assisted state: SavedStateHandle
) : ViewModel() {

//    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    private val galleryEventsChannel = Channel<GalleryEvents>()
    val galleryEvent = galleryEventsChannel.receiveAsFlow()

//    private val currentQuerySave = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)
//    val photos = currentQuery.switchMap { queryString ->
//        repository.getSearchResults(queryString).cachedIn(viewModelScope)
//    }

    val photos = repository.getSearchResults(DEFAULT_QUERY).cachedIn(viewModelScope)

    val isActive = repository.getIsActive()

//    fun searchPhotos(query: String) {
//        currentQuery.value = query
//    }

    fun onBookmarkClick(data: SoccerVideos, context: Context){
        saveGalleryBookmark(data,context)
    }

    private fun saveGalleryBookmark(data: SoccerVideos, context: Context) = viewModelScope.launch{
        val bookmark = BookMarkData(
            data.id,
            data.competition,
            data.title,
            data.thumbnail,
            data.video,
            data.thumbnail,
            data.title + context.getString(R.string.dont_miss_the_action),
            "",
            "",
            "",
            System.currentTimeMillis(),
            false,
            SortOrder.BY_HIGHLIGHTS
        )
        val isSave = bookmarkDao.getBookmark(data.title!!)

        if (isSave == null){
            bookmarkDao.insert(bookmark)
            galleryEventsChannel.send(GalleryEvents.SaveHighlights("${data.title} ${context.getString(R.string.is_save)}"))
        }else{
            galleryEventsChannel.send(GalleryEvents.AllreadySaveHighlights("${data.title} ${context.getString(R.string.is_already_save)}"))
        }
    }

    companion object {
        private  const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = "cats"
    }

    sealed class GalleryEvents {
        data class SaveHighlights(val data: String) : GalleryEvents()
        data class AllreadySaveHighlights(val data: String) : GalleryEvents()
    }
}