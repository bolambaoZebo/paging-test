package com.example.secondhiltapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.secondhiltapp.db.entity.SoccerNews

@Dao
interface SoccerChineseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savaAllSoccerChinese(soccerChineseList: List<SoccerNews>)

    @Query("SELECT * FROM soccer_chinese_news")
    fun getSoccerChinese() : LiveData<List<SoccerNews>>
}