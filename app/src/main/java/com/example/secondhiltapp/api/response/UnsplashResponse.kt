package com.example.secondhiltapp.api.response

import com.example.secondhiltapp.data.UnsplashPhoto


data class UnsplashResponse(
    val results: List<UnsplashPhoto>
)