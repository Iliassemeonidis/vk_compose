package com.example.myapplication.data.repository

import android.content.Context
import com.example.myapplication.data.mapper.NewsFeedMapper
import com.example.myapplication.data.mapper.NewsFeedMapper.mapResponseToComment
import com.example.myapplication.data.network.ApiFactory
import com.example.myapplication.domain.FeedPost
import com.example.myapplication.domain.PostComment
import com.example.myapplication.domain.StatisticsItem
import com.example.myapplication.domain.StatisticsType
import com.example.myapplication.extention.mergeWith
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class NewsFeedRepository(application: Context) {

    private val keyValueStorage = VKPreferencesKeyValueStorage(application)
    private val token = VKAccessToken.restore(keyValueStorage)

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val flowEvent = MutableSharedFlow<Unit>(1)

    private val refreshListFlow = MutableSharedFlow<List<FeedPost>>()

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()


    private val loadListFeedPost = flow {
        flowEvent.emit(Unit)
        flowEvent.collect {
            val startFrom = nextFrom

            if (startFrom == null && feedPosts.isNotEmpty()) {
                emit(feedPosts)
                return@collect
            }
            val responseDto =
                if (startFrom == null) {
                    ApiFactory.apiService.loadUserNewsfeed(getAccessToken())
                } else {
                    ApiFactory.apiService.loadUserNewsfeed(getAccessToken(), startFrom)
                }
            nextFrom = responseDto.newsFeed.nextFrom
            val result = NewsFeedMapper.mapResponseToPosts(responseDto)
            _feedPosts.addAll(result)
            emit(feedPosts)
        }
    }

    private var nextFrom: String? = null

    val wallListFeedPost: StateFlow<List<FeedPost>> = loadListFeedPost
        .mergeWith(refreshListFlow)
        .stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = feedPosts
    )

    suspend fun loadNextData() {
        flowEvent.emit(Unit)
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
        refreshListFlow.emit(feedPosts)
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
        refreshListFlow.emit(feedPosts)
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