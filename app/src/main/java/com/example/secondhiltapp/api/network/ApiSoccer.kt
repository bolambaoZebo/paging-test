package com.example.secondhiltapp.api.network

import com.example.secondhiltapp.BuildConfig
import com.example.secondhiltapp.api.response.SoccerNewsResponse
import com.example.secondhiltapp.api.response.SoccerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiSoccer {

    companion object {
        const val BASE_URL = "https://sleepy-turing-6de1dd.netlify.app/"//"https://api.unsplash.com/"
        const val CLIENT_ID = BuildConfig.UNSPLASH_ACCESS_KEY
    }

//    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
//    @GET("search/photos")
//    suspend fun searchPhotos(
//        @Query("query") query: String,
//        @Query("page") page: Int,
//        @Query("per_page") perPage: Int
//    ): UnsplashResponse

    @GET(".netlify/functions/api")
    suspend fun getSoccerList() : Response<SoccerNewsResponse>

    @GET(".netlify/functions/api/video/paging")
    suspend fun getSoccer(
        @Query("page") pageIndex: Int,
        @Query("limit") pageLimit: Int
    ): SoccerResponse
}