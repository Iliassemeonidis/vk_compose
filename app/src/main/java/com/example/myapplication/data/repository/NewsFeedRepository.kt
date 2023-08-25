package com.example.myapplication.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.myapplication.data.mapper.NewsFeedMapper
import com.example.myapplication.data.mapper.NewsFeedMapper.mapResponseToComment
import com.example.myapplication.data.network.ApiFactory
import com.example.myapplication.domain.FeedPost
import com.example.myapplication.domain.PostComment
import com.example.myapplication.domain.StatisticsItem
import com.example.myapplication.domain.StatisticsType
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlin.math.absoluteValue

class NewsFeedRepository(application: Context) {

    private val keyValueStorage = VKPreferencesKeyValueStorage(application)
    private val token = VKAccessToken.restore(keyValueStorage)

    private val _feedPosts = mutableListOf<FeedPost>()
    val feedPost: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    suspend fun loadFeedPost(): List<FeedPost> {
        val startFrom = nextFrom

        if (startFrom == null && feedPost.isNotEmpty()) return feedPost
        val responseDto =
            if (startFrom == null) {
                ApiFactory.apiService.loadUserNewsfeed(getAccessToken())
            } else {
                ApiFactory.apiService.loadUserNewsfeed(getAccessToken(), startFrom)
            }
        nextFrom = responseDto.newsFeed.nextFrom
        val result = NewsFeedMapper.mapResponseToPosts(responseDto)
        _feedPosts.addAll(result)
        return feedPost
    }

    private fun getAccessToken(): String {
        return token?.accessToken ?: throw IllegalAccessException("Token is null")
    }

    suspend fun changeLikeStatus(feedPost: FeedPost) {
        val response = if (feedPost.isLiked) {
            ApiFactory.apiService.deleteLike(
                token = getAccessToken(),
                itemId = feedPost.postId,
                postId = feedPost.id,
            )
        } else {
            ApiFactory.apiService.addLike(
                token = getAccessToken(),
                itemId = feedPost.postId,
                postId = feedPost.id
            )
        }

        val newLikesCount = response.likes.likesCount
        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticsType.FAVORITE }
            add(StatisticsItem(StatisticsType.FAVORITE, newLikesCount))
        }

        val newFeedPost = feedPost.copy(statistics = newStatistics, isLiked = !feedPost.isLiked)
        val newPostIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[newPostIndex] = newFeedPost
    }


    suspend fun deleteFeedFromList(feedPost: FeedPost) {
        val response = ApiFactory.apiService.ignoreFeed(
            token = getAccessToken(),
            itemId = feedPost.postId,
            postId = feedPost.id,
        )
        if (response.response == 1) {
            _feedPosts.remove(feedPost)
        } else {
            throw IllegalAccessException("Feed post cannot be deleted")
        }
    }

    suspend fun getFeedPostsComment(feedPost: FeedPost): List<PostComment> {
        val response = ApiFactory.apiService.getComments(
            token = getAccessToken(),
            postId = feedPost.postId,
            ownerId = feedPost.id,
            extended = 1,
            fields = feedPost.userPhoto
        )
        return mapResponseToComment(response)
    }

}