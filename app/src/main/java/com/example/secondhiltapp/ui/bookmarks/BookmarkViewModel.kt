package com.example.secondhiltapp.ui.bookmarks

import androidx.hilt.Assisted
import androidx.lifecycle.*
import com.example.secondhiltapp.data.RoomRepository
import com.example.secondhiltapp.data.SoccerRepository
import com.example.secondhiltapp.db.BookmarkDao
import com.example.secondhiltapp.preferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val repository: SoccerRepository,
    private val soccerRepository: RoomRepository,
    @Assisted private val state: SavedStateHandle,
    private val preferencesManager: PreferencesManager,
    private val bookmarkDao: BookmarkDao
) : ViewModel() {

    //val soccers = soccerRepository.getSoccerNews().asLiveData()


    val searchQuery = state.getLiveData("searchQuery", "")

    val preferencesFlow = preferencesManager.preferencesFlow

    private val bookmarkEventChannel = Channel<TasksEvent>()
    val tasksEvent = bookmarkEventChannel.receiveAsFlow()

    private val bookmarksFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        bookmarkDao.getBookmarks(query,filterPreferences.sortOrder, filterPreferences.hideCompleted)//taskDao.getTasks(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
    }

    val bookmark = bookmarksFlow.asLiveData()

    sealed class TasksEvent {
        object NavigateToAddTaskScreen : TasksEvent()
//        data class NavigateToEditTaskScreen(val task: Task) : TasksEvent()
//        data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
//        data class ShowTaskSavedConfirmationMessage(val msg: String) : TasksEvent()
//        object NavigateToDeleteAllCompletedScreen : TasksEvent()
    }

}