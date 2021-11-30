package com.example.secondhiltapp.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.secondhiltapp.R
import com.example.secondhiltapp.R.color
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout



fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun TabLayout.tabShow(){
    visibility = View.VISIBLE
}

fun TabLayout.tabHide(){
    visibility = View.GONE
}

fun RecyclerView.show(){
    visibility = View.VISIBLE
}

fun RecyclerView.hide(){
    visibility = View.GONE
}

fun View.slideUp(duration: Int = 500) {
    val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.alpha = 0f
    this.startAnimation(animate)
    visibility = View.GONE
}

fun View.slideDown(duration: Int = 500) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
}

//@SuppressLint("ResourceAsColor")
//fun View.snackbar(message: String, save: Boolean = true){
//
//    val snackbarView =  Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
//        if(save){
//            snackbar.setAction(resources.getString(R.string.ok)){
//                snackbar.dismiss()
//            }
//        }
//    }
//    snackbarView.show()
//}

fun ImageView.show(){
    visibility = View.VISIBLE
}

fun ImageView.hide(){
    visibility = View.GONE
}

inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}