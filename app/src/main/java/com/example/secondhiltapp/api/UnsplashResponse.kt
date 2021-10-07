package com.example.secondhiltapp.api

import com.example.secondhiltapp.data.UnsplashPhoto


data class UnsplashResponse(
    val results: List<UnsplashPhoto>
)