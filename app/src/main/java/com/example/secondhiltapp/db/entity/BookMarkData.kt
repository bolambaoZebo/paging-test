package com.example.secondhiltapp.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

private const val BOOKMARK_ID = 0

@Entity(tableName = "bookmark")
@Parcelize
data class BookMarkData(
    var id: String? = null,
    var competition: String? = null,
    var title: String? = null,
    var thumbnail: String? = null,
    var video: String? = null,
    var imageUrl: String? = null,
    var description: String? = null,
    var teamLeague: String? = null,
    var titleChinese: String? = null,
    var descriptionChinese: String? = null,
    var created: Long = System.currentTimeMillis(),
    var isHidden: Boolean = true,
    var type: String? = null //news or highlights
) : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var uid: Int = BOOKMARK_ID

    val createdDateFormat: String
        get() = DateFormat.getDateTimeInstance().format(created)
}
