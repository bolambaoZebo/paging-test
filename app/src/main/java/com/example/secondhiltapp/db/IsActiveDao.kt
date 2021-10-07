package com.example.secondhiltapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.secondhiltapp.db.entity.IsClickable
import com.example.secondhiltapp.db.entity.LanguageData

@Dao
interface IsActiveDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveIsActive(language: IsClickable)

    @Query("SELECT * FROM clickable")
    fun getIsActive() : LiveData<IsClickable>

    @Query("DELETE FROM clickable")
    suspend fun deleteIsActive()
}