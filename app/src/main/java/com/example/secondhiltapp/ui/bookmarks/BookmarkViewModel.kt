package com.example.secondhiltapp.ui.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.secondhiltapp.data.RoomRepository
import com.example.secondhiltapp.data.SoccerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val repository: SoccerRepository,
    private val soccerRepository: RoomRepository
) : ViewModel() {

    val soccers = soccerRepository.getSoccerNews().asLiveData()
}