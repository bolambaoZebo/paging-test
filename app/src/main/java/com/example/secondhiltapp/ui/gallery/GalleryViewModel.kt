package com.example.secondhiltapp.ui.gallery

import androidx.hilt.Assisted
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
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
    private val repository: SoccerRepository,
    private val bookmarkDao: BookmarkDao,
    @Assisted state: SavedStateHandle
) : ViewModel() {

    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    private val galleryEventsChannel = Channel<GalleryEvents>()
    val galleryEvent = galleryEventsChannel.receiveAsFlow()

    private val currentQuerySave = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)
//    val photos = currentQuery.switchMap { queryString ->
//        repository.getSearchResults(queryString).cachedIn(viewModelScope)
//    }

    val photos = repository.getSearchResults(DEFAULT_QUERY).cachedIn(viewModelScope)

    val isActive = repository.getIsActive()

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

    fun onBookmarkClick(data: SoccerVideos){
        saveGalleryBookmark(data)
    }

    private fun saveGalleryBookmark(data: SoccerVideos) = viewModelScope.launch{
        val bookmark = BookMarkData(
            data.id,
            data.competition,
            data.title,
            data.thumbnail,
            data.video,
            data.thumbnail,
            "",
            "",
            "",
            "",
            System.currentTimeMillis(),
            false,
            SortOrder.BY_HIGHLIGHTS
        )
        bookmarkDao.insert(bookmark)
        galleryEventsChannel.send(GalleryEvents.SaveHighlights(data))
    }

    companion object {
        private  const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = "cats"
    }

    sealed class GalleryEvents {
        data class SaveHighlights(val data: SoccerVideos) : GalleryEvents()
    }
}