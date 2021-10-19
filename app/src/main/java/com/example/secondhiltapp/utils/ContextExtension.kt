package com.example.secondhiltapp.utils

import android.app.Activity
import android.content.Context
import com.example.secondhiltapp.R
import com.google.android.material.snackbar.Snackbar

fun Context.snackBar(msg: String, activity: Activity){
    Snackbar.make(activity.findViewById(R.id.bottom_nav), msg, Snackbar.LENGTH_LONG).apply {
    }
        .setAction("OK"){}
        .show()
}