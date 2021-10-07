package com.example.secondhiltapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
const val CURRENT_USER_ID = 0

@Entity(tableName = "soccer_news")
data class SoccerNews(
    var id: String? = null,
    var imageUrl: String? = null,
    var title: String? = null,
    var description: String? = null,
    var teamLeague: String? = null,
    var titleChinese: String? = null,
    var descriptionChinese: String? = null,
    var language: String? = null
){
    @PrimaryKey(autoGenerate = true)
    var uid: Int = CURRENT_USER_ID
}



