package com.example.myapplication.domain.repository

import com.example.myapplication.domain.entity.FeedPost
import com.example.myapplication.domain.entity.LoginAppState
import com.example.myapplication.presintation.comment.CommentsState
import kotlinx.coroutines.flow.StateFlow

interface NewsFeedRepository {

    fun getAuthStateFlow(): StateFlow<LoginAppState>

    fun getWallListFeedPost(): StateFlow<List<FeedPost>>

    fun getFeedPostsComment(feedPost: FeedPost): StateFlow<CommentsState>

    suspend fun loadNextData()

    suspend fun checkAuthState()

    suspend fun deletePost(feedPost: FeedPost)

    suspend fun changeLikeStatus(feedPost: FeedPost)
}