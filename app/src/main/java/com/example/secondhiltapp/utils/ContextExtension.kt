package com.example.secondhiltapp.utils

import android.app.Activity
import android.content.Context
import com.example.secondhiltapp.R
import com.google.android.material.snackbar.Snackbar

fun Context.snackBar(msg: String,activity: Activity, save: Boolean = true){
    Snackbar.make(activity.findViewById(R.id.bottom_nav), msg, Snackbar.LENGTH_LONG).apply {
        if (save){
            this.setAction("OK"){}
        }
    }.show()
}

val <T> T.exhaustive: T
    get() = this