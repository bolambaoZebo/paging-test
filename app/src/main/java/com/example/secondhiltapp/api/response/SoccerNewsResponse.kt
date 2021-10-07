package com.example.secondhiltapp.api.response

import com.example.secondhiltapp.db.entity.SoccerNews

data class SoccerNewsResponse(
    val isActive: Boolean?,
    val en: List<SoccerNews>?,
    val zh: List<SoccerNews>?
)
