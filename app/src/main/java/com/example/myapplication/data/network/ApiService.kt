package com.example.myapplication.data.network

import com.example.myapplication.data.model.NewsFeedResponseDto
import com.example.myapplication.navigation.Screens
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    //@GET("newsfeed.get?v=5.131")
    @GET("newsfeed.getRecommended?v=5.131")
    suspend fun loadUserNewsfeed(@Query("access_token") token: String)
            : NewsFeedResponseDto

}
