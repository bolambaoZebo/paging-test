package com.example.secondhiltapp.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.secondhiltapp.api.network.ApiSoccer
import com.example.secondhiltapp.db.AppRoom
import com.example.secondhiltapp.db.entity.IsClickable
import kotlinx.coroutines.flow.filter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoccerRepository @Inject constructor(
    private val apiSoccer: ApiSoccer,
    private val db: AppRoom
) {

    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 12,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UnsplashPagingSource(apiSoccer, query, db) }
        ).liveData

    fun getIsActive() = db.getIsActiveDao().getIsActive()


}