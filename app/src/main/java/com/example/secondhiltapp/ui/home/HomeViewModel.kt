package com.example.secondhiltapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.secondhiltapp.data.RoomRepository
import com.example.secondhiltapp.data.SoccerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val soccerRepository: RoomRepository
) : ViewModel() {

    fun currentLang() = soccerRepository.getLanguageNow()
    val soccerData = soccerRepository.getSoccerNews().asLiveData()
}