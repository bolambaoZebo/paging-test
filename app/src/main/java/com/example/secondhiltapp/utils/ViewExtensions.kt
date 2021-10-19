package com.example.secondhiltapp.utils

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.secondhiltapp.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout


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

fun View.snackbar(message: String){
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction(resources.getString(R.string.ok)){
            snackbar.dismiss()
        }
    }.show()
}

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