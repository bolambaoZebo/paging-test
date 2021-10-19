package com.example.secondhiltapp.db

import androidx.room.*
import com.example.secondhiltapp.db.entity.BookMarkData
import com.example.secondhiltapp.preferences.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    fun getBookmarks(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<BookMarkData>> =
        when (sortOrder) {
            SortOrder.BY_DATE -> getTasksSortedByName(query)
            SortOrder.BY_NAME -> getAllBookmark(query)
            SortOrder.BY_NEWS -> getAllNews(sortOrder,query)
            SortOrder.BY_HIGHLIGHTS -> getAllHighlights(sortOrder,query)
        }
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savaBookmark(soccerNews: List<BookMarkData>)

    @Query("DELETE FROM bookmark")
    suspend fun deleteAllBookmark()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookMarkData: BookMarkData)

    @Query("SELECT * FROM bookmark WHERE title LIKE '%' || :searchQuery || '%' ORDER BY title")
    fun getAllBookmark(searchQuery: String) : Flow<List<BookMarkData>>

    @Query("SELECT * FROM bookmark WHERE title LIKE '%' || :title || '%'")
    fun getBookmark(title: String) : BookMarkData

    @Delete
    suspend fun delete(bookMarkData: BookMarkData)

    @Query("SELECT * FROM bookmark WHERE title LIKE '%' || :searchQuery || '%' ORDER BY created DESC")
    fun getTasksSortedByName(searchQuery: String): Flow<List<BookMarkData>>

    @Query("SELECT * FROM bookmark WHERE type == :sortOrder AND title LIKE '%' || :searchQuery || '%' ORDER BY created DESC")
    fun getAllNews(sortOrder: SortOrder,searchQuery: String): Flow<List<BookMarkData>>

    @Query("SELECT * FROM bookmark WHERE type == :sortOrder AND title LIKE '%' || :searchQuery || '%'ORDER BY created DESC")
    fun getAllHighlights(sortOrder: SortOrder,searchQuery: String): Flow<List<BookMarkData>>

}