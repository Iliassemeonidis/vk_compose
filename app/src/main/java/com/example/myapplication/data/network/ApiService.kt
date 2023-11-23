package com.example.myapplication.data.network

import com.example.myapplication.data.model.IgnoreFeedPost
import com.example.myapplication.data.model.NewsFeedResponseDto
import com.example.myapplication.data.model.ResponseCommentDto
import com.example.myapplication.data.model.ResponseLikesDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    //    @GET("newsfeed.get?v=5.131") ывфы
    //    Тут новый  комент
    @GET("newsfeed.getRecommended?v=5.131")
    suspend fun loadUserNewsfeed(@Query("access_token") token: String)
            : NewsFeedResponseDto

    //    @GET("newsfeed.get?v=5.131")
    @GET("newsfeed.getRecommended?v=5.131")
    suspend fun loadUserNewsfeed(
        @Query("access_token") token: String,
        @Query("start_from") startFrom: String
    ): NewsFeedResponseDto

    @GET("likes.add?v=5.131&type=post")
    suspend fun addLike(
        @Query("access_token") token: String,
        @Query("item_id") itemId: Long,
        @Query("owner_id") postId: Long,
    ): ResponseLikesDto

    @GET("likes.delete?v=5.131&type=post")
    suspend fun deleteLike(
        @Query("access_token") token: String,
        @Query("item_id") itemId: Long,
        @Query("owner_id") postId: Long,
    ): ResponseLikesDto

    @GET("newsfeed.ignoreItem?v=5.131&type=wall")
    suspend fun ignoreFeed(
        @Query("access_token") token: String,
        @Query("item_id") itemId: Long,
        @Query("owner_id") postId: Long,
    ): IgnoreFeedPost

    @GET("wall.getComments?v=5.131")
    suspend fun getComments(
        @Query("access_token") token: String,
        @Query("post_id") postId: Long,
        @Query("owner_id") ownerId: Long,
        @Query("extended") extended: Int,
        @Query("fields") fields : String
    ): ResponseCommentDto

}
