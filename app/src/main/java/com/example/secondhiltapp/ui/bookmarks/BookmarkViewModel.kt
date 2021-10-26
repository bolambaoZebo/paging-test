package com.example.secondhiltapp.ui.bookmarks

import androidx.hilt.Assisted
import androidx.lifecycle.*
import com.example.secondhiltapp.data.RoomRepository
import com.example.secondhiltapp.data.SoccerRepository
import com.example.secondhiltapp.db.BookmarkDao
import com.example.secondhiltapp.db.entity.BookMarkData
import com.example.secondhiltapp.preferences.PreferencesManager
import com.example.secondhiltapp.preferences.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val repository: SoccerRepository,
    private val soccerRepository: RoomRepository,
    @Assisted private val state: SavedStateHandle,
    private val preferencesManager: PreferencesManager,
    private val bookmarkDao: BookmarkDao
) : ViewModel() {

    val isActive = repository.getIsActive()
    //val soccers = soccerRepository.getSoccerNews().asLiveData()
    fun currentLang() = soccerRepository.getLanguageNow()

    val searchQuery = state.getLiveData("searchQuery", "")

    val preferencesFlow = preferencesManager.preferencesFlow

    private val bookmarkEventChannel = Channel<BookmarkEvent>()
    val bookmarkEvent = bookmarkEventChannel.receiveAsFlow()

    private val bookmarksFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        bookmarkDao.getBookmarks(query,filterPreferences.sortOrder, filterPreferences.hideCompleted)//taskDao.getTasks(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
    }

    val bookmark = bookmarksFlow.stateIn(viewModelScope, SharingStarted.Lazily, null)//asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onTaskSwiped(bookMarkData: BookMarkData) = viewModelScope.launch {
        bookmarkDao.delete(bookMarkData)
        bookmarkEventChannel.send(BookmarkEvent.ShowUndoDeleteTaskMessage(bookMarkData))//tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }

    fun onUndoDeleteClick(bookMarkData: BookMarkData) = viewModelScope.launch {
        bookmarkDao.insert(bookMarkData)
    }

    fun onDeleteAllCompletedClick() = viewModelScope.launch {
        bookmarkEventChannel.send(BookmarkEvent.NavigateToDeleteAllCompletedScreen)
    }

    sealed class BookmarkEvent {
        object NavigateToAddTaskScreen : BookmarkEvent()
        data class ShowUndoDeleteTaskMessage(val bookMarkData: BookMarkData) : BookmarkEvent()
//        data class NavigateToEditTaskScreen(val task: Task) : TasksEvent()
//        data class ShowTaskSavedConfirmationMessage(val msg: String) : TasksEvent()
        object NavigateToDeleteAllCompletedScreen : BookmarkEvent()
    }

}