package com.example.secondhiltapp.ui.gallery

import androidx.hilt.Assisted
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.secondhiltapp.data.SoccerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: SoccerRepository,
    @Assisted state: SavedStateHandle
) : ViewModel() {

    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    private val currentQuerySave = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

//    val photos = currentQuery.switchMap { queryString ->
//        repository.getSearchResults(queryString).cachedIn(viewModelScope)
//    }

    val photos = repository.getSearchResults(DEFAULT_QUERY).cachedIn(viewModelScope)

    val isActive = repository.getIsActive()

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

    companion object {
        private  const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = "cats"
    }
}