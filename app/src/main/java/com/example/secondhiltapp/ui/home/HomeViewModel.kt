package com.example.secondhiltapp.ui.home

import androidx.lifecycle.*
import com.example.secondhiltapp.data.RoomRepository
import com.example.secondhiltapp.data.SoccerRepository
import com.example.secondhiltapp.db.BookmarkDao
import com.example.secondhiltapp.db.entity.BookMarkData
import com.example.secondhiltapp.db.entity.SoccerNews
import com.example.secondhiltapp.preferences.BOOKMARKTYPE
import com.example.secondhiltapp.preferences.SortOrder
import com.example.secondhiltapp.ui.gallery.GalleryViewModel
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

    var sliderImageUrl = mutableListOf<String>(
        "https://soccer-news.s3.ap-southeast-1.amazonaws.com/uno.png",
        "https://soccer-news.s3.ap-southeast-1.amazonaws.com/dos.png",
        "https://soccer-news.s3.ap-southeast-1.amazonaws.com/tres.png",
        "https://soccer-news.s3.ap-southeast-1.amazonaws.com/tres.png"
    )
    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()


    //METHODS
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
            SortOrder.BY_NEWS
        )

        if (bookMarkData != null){
            saveBookmark(data)
        }


    }

    private fun saveBookmark(data: BookMarkData) = viewModelScope.launch {
        val isSave = bookmarkDao.getBookmark(data.title!!)
        if (isSave == null){
            bookmarkDao.insert(data)
            addEditTaskEventChannel.send(AddEditTaskEvent.SaveBookmark("${data.title} is saved"))
        }else{
            addEditTaskEventChannel.send(AddEditTaskEvent.AlreadySaved("${data.title} is already saved"))
        }
    }

    sealed class AddEditTaskEvent {
//        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
//        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
        data class SaveBookmark(val msg: String) : AddEditTaskEvent()
        data class AlreadySaved(val msg: String) : AddEditTaskEvent()
    }
}