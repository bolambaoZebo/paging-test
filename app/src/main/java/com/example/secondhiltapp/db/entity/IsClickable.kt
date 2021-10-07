package com.example.secondhiltapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val ID_CLICK = 0

@Entity(tableName = "clickable")
data class IsClickable(
    var isActive: Boolean? = null
){
    @PrimaryKey(autoGenerate = false)
    var uid: Int = ID_CLICK
}