package com.example.secondhiltapp.api.response

import com.example.secondhiltapp.data.SoccerVideos


data class SoccerResponse(
    val isActive: Boolean?,
    val data: List<SoccerVideos>
)
