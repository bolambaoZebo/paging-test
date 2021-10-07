package com.example.secondhiltapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val LANG_ID = 0

@Entity(tableName = "languages")
data class LanguageData(
    var language: String = "en"
){
    @PrimaryKey(autoGenerate = false)
    var uid: Int = LANG_ID
}
