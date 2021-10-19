package com.example.secondhiltapp

import androidx.lifecycle.ViewModel
import com.example.secondhiltapp.data.RoomRepository
import com.example.secondhiltapp.db.entity.LanguageData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.intellij.lang.annotations.Language
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {

    fun currentLang() = roomRepository.getLanguageNow()

    fun setLanguage(lang: LanguageData){
        roomRepository.saveLanguage(lang)
    }
}