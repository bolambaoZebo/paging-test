package com.example.secondhiltapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SoccerVideos(
    var id: String? = null,
    var competition: String? = null,
    var title: String? = null,
    var thumbnail: String? = null,
    var video: String? = null,
    var date: String? = null
): Parcelable