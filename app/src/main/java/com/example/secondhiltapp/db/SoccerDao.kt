package com.example.secondhiltapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.secondhiltapp.db.entity.SoccerNews
import kotlinx.coroutines.flow.Flow

@Dao
interface SoccerDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savaAllSoccer(soccerNews: List<SoccerNews>)

    @Query("DELETE FROM soccer_news")
    suspend fun deleteAllSoccerNews()

    @Query("SELECT * FROM soccer_news")
    fun getSoccer() : Flow<List<SoccerNews>>



//    LiveData<List<SoccerNews>>
//    @Query("SELECT * FROM soccer_news WHERE teamLeague LIKE :searchQuery")
//    fun searchDatabase(searchQuery: String): LiveData<List<SoccerNews>>

}