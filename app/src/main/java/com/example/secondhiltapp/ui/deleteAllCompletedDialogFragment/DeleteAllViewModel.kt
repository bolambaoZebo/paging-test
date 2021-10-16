package com.example.secondhiltapp.ui.deleteAllCompletedDialogFragment

import androidx.lifecycle.ViewModel
import com.example.secondhiltapp.db.BookmarkDao
import com.example.secondhiltapp.di.ApplicationScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteAllViewModel @Inject constructor(
    private val bookmarkDao: BookmarkDao,
    @ApplicationScope private val applicationScope: CoroutineScope
): ViewModel() {

    fun onConfirmClick() = applicationScope.launch {
        bookmarkDao.deleteAllBookmark()
    }
}