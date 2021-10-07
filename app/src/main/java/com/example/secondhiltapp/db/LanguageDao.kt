package com.example.secondhiltapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.secondhiltapp.db.entity.LanguageData

@Dao
interface LanguageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveLanguage(language: LanguageData)

    @Query("SELECT * FROM languages")
    fun getLanguage() : LiveData<LanguageData>
}