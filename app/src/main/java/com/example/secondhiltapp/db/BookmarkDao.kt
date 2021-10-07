package com.example.secondhiltapp.db

import androidx.room.*
import com.example.secondhiltapp.db.entity.BookMarkData
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savaBookmark(soccerNews: List<BookMarkData>)

    @Query("DELETE FROM bookmark")
    suspend fun deleteAllBookmark()

    @Query("SELECT * FROM bookmark")
    fun getAllBookmark() : Flow<List<BookMarkData>>

//    fun getTasks(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>> =
//        when (sortOrder) {
//            SortOrder.BY_DATE -> getTasksSortedByDateCreated(query, hideCompleted)
//            SortOrder.BY_NAME -> getTasksSortedByName(query, hideCompleted)
//        }
//
//    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, name")
//    fun getTasksSortedByName(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>
//
//    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, created")
//    fun getTasksSortedByDateCreated(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(task: Task)
//
//    @Update
//    suspend fun update(task: Task)
//
//    @Delete
//    suspend fun delete(task: Task)
//
//    @Query("DELETE FROM task_table WHERE completed = 1")
//    suspend fun deleteCompletedTasks()

}