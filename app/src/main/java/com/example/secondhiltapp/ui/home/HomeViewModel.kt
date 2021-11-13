package com.example.secondhiltapp.ui.home

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.secondhiltapp.data.RoomRepository
import com.example.secondhiltapp.data.SoccerRepository
import com.example.secondhiltapp.db.BookmarkDao
import com.example.secondhiltapp.db.SoccerDao
import com.example.secondhiltapp.db.entity.BookMarkData
import com.example.secondhiltapp.db.entity.SoccerNews
import com.example.secondhiltapp.preferences.SortOrder
import com.example.secondhiltapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val soccerRepository: RoomRepository,
    repository: SoccerRepository,
    private val bookmarkDao: BookmarkDao
) : ViewModel() {

    fun currentLang() = soccerRepository.getLanguageNow()

    val isActive = repository.getIsActive()

    var isScrolling = MutableLiveData<Boolean>(false)

    private val refreshTriggerChannel = Channel<Refresh>()
    private val refreshTrigger = refreshTriggerChannel.receiveAsFlow()

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    var pendingScrollToTopAfterRefresh = false

    val soccerData = refreshTrigger.flatMapLatest { refresh ->
        soccerRepository.getSoccerNews(
            refresh == Refresh.FORCE,
            onFetchSuccess = {
                pendingScrollToTopAfterRefresh = true
            },
            onFetchFailed = { t ->
                viewModelScope.launch {
                    addEditTaskEventChannel.send(
                        AddEditTaskEvent.ShowErrorMessage(
                            t
                        )
                    )
                }
            }
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)


    fun onStart() {
        if (soccerData.value !is Resource.Loading) {
            viewModelScope.launch {
                refreshTriggerChannel.send(Refresh.NORMAL)
            }
        }
    }

    fun onManualRefresh() {
        if (soccerData.value !is Resource.Loading) {
            viewModelScope.launch {
                refreshTriggerChannel.send(Refresh.FORCE)
            }
        }
    }

    var sliderImageUrl = mutableListOf<String>(
        "https://96group.s3.ap-southeast-1.amazonaws.com/app+1.png",
        "https://96group.s3.ap-southeast-1.amazonaws.com/app2.png",
        "https://96group.s3.ap-southeast-1.amazonaws.com/app3.png",
        "https://96group.s3.ap-southeast-1.amazonaws.com/app4.png"
    )

    //METHODS
    fun onSaveNews(bookMarkData: SoccerNews) {
        var data = convertToBookMark(bookMarkData)
        saveBookmark(data)
    }

    fun convertToBookMark(soccer: SoccerNews): BookMarkData {
        return BookMarkData(
            soccer.id,
            "",
            soccer.title,
            soccer.imageUrl,
            "",
            soccer.imageUrl,
            soccer.description,
            soccer.teamLeague,
            soccer.titleChinese,
            soccer.descriptionChinese,
            System.currentTimeMillis(),
            false,
            SortOrder.BY_NEWS
        )
    }

    private fun saveBookmark(data: BookMarkData) = viewModelScope.launch {
        val isSave = bookmarkDao.getBookmark(data.title!!)
        if (isSave == null) {
            bookmarkDao.insert(data)
            addEditTaskEventChannel.send(AddEditTaskEvent.SaveBookmark("${data.title} is saved"))
        } else {
            addEditTaskEventChannel.send(AddEditTaskEvent.AlreadySaved("${data.title} is already saved"))
        }
    }

    enum class Refresh {
        FORCE, NORMAL
    }

    sealed class AddEditTaskEvent {
        data class SaveBookmark(val msg: String) : AddEditTaskEvent()
        data class AlreadySaved(val msg: String) : AddEditTaskEvent()
        data class ShowErrorMessage(val error: Throwable) : AddEditTaskEvent()
    }

}


//    val soccerData = soccerRepository.getSoccerNews().stateIn(viewModelScope, SharingStarted.Lazily, null )
//    init {
//        viewModelScope.launch {
//            soccerRepository.deleteNonBookmarkedArticlesOlderThan(
//                System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
//            )
//        }
//    }

//,
//    private val soccerDao: SoccerDao
//    val radomUserPostFlow = Pager(PagingConfig(5)){ SoccerNewsPaging(soccerDao)}
//        .flow
//        .cachedIn(viewModelScope)

//        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
//        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()

//
//            BookMarkData(
//            bookMarkData.id,
//            "",
//            bookMarkData.title,
//            bookMarkData.imageUrl,
//            "",
//            bookMarkData.imageUrl,
//            bookMarkData.description,
//            bookMarkData.teamLeague,
//            bookMarkData.titleChinese,
//            bookMarkData.descriptionChinese,
//            System.currentTimeMillis(),
//            false,
//            SortOrder.BY_NEWS
//        )