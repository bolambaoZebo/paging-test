package com.example.secondhiltapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.secondhiltapp.api.network.ApiSoccer
import com.example.secondhiltapp.db.AppRoom
import com.example.secondhiltapp.db.entity.IsClickable
import retrofit2.HttpException
import java.io.IOException

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

class UnsplashPagingSource(
    private val apiSoccer: ApiSoccer,
    private val query: String,
    private val db: AppRoom
) : PagingSource<Int, SoccerVideos>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SoccerVideos> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {
        val response = apiSoccer.getSoccer(position, params.loadSize)//searchPhotos(query, position, params.loadSize)
        val photos = response.data
        val isActiveNow = response.isActive

            if (position == UNSPLASH_STARTING_PAGE_INDEX) {
                db.getIsActiveDao().deleteIsActive()
                db.getIsActiveDao().saveIsActive(IsClickable(isActiveNow))
            }

            LoadResult.Page(
                data = photos,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SoccerVideos>): Int? {
        TODO("Not yet implemented")
    }
}