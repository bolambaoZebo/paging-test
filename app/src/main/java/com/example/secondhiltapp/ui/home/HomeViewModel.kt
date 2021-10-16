package com.example.secondhiltapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.secondhiltapp.data.RoomRepository
import com.example.secondhiltapp.data.SoccerRepository
import com.example.secondhiltapp.db.BookmarkDao
import com.example.secondhiltapp.db.entity.BookMarkData
import com.example.secondhiltapp.db.entity.SoccerNews
import com.example.secondhiltapp.preferences.BOOKMARKTYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val soccerRepository: RoomRepository,
    private val bookmarkDao: BookmarkDao
) : ViewModel() {

    fun currentLang() = soccerRepository.getLanguageNow()
    val soccerData = soccerRepository.getSoccerNews().asLiveData()

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    fun onSaveNews(bookMarkData: SoccerNews){
        var data = BookMarkData(
            bookMarkData.id,
            "",
            bookMarkData.title,
            bookMarkData.imageUrl,
            "",
            bookMarkData.imageUrl,
            bookMarkData.description,
            bookMarkData.teamLeague,
            bookMarkData.titleChinese,
            bookMarkData.descriptionChinese,
            System.currentTimeMillis(),
            false,
            BOOKMARKTYPE.NEWS
        )
        if (bookMarkData != null){
            saveBookmark(data)
        }
    }

    private fun saveBookmark(data: BookMarkData) = viewModelScope.launch {
        bookmarkDao.insert(data)
//        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
        addEditTaskEventChannel.send(AddEditTaskEvent.SaveBookmark("${data.title} is save"))
    }

    sealed class AddEditTaskEvent {
//        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
//        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
        data class SaveBookmark(val msg: String) : AddEditTaskEvent()
    }
}