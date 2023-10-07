package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.mapper.NewsFeedMapper
import com.example.myapplication.data.network.ApiService
import com.example.myapplication.domain.entity.FeedPost
import com.example.myapplication.domain.entity.LoginAppState
import com.example.myapplication.domain.entity.StatisticsItem
import com.example.myapplication.domain.entity.StatisticsType
import com.example.myapplication.domain.repository.NewsFeedRepository
import com.example.myapplication.extention.mergeWith
import com.example.myapplication.presintation.comment.CommentsState
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class NewsFeedRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val newsFeedMapper: NewsFeedMapper,
    private val keyValueStorage: VKPreferencesKeyValueStorage
    ) : NewsFeedRepository {

    private val token
        get() = VKAccessToken.restore(keyValueStorage)

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val nextDataNededEvent = MutableSharedFlow<Unit>(1)
    private val currentTokenStatus = MutableSharedFlow<Unit>(1)
    private val refreshListFlow = MutableSharedFlow<List<FeedPost>>()

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    private fun getAccessToken(): String {
        return token?.accessToken ?: throw IllegalAccessException("Token is null")
    }

    private val loadListFeedPost = flow {
        nextDataNededEvent.emit(Unit)
        nextDataNededEvent.collect {
            val startFrom = nextFrom

            if (startFrom == null && feedPosts.isNotEmpty()) {
                emit(feedPosts)
                return@collect
            }
            val responseDto =
                if (startFrom == null) {
                    apiService.loadUserNewsfeed(getAccessToken())
                } else {
                    apiService.loadUserNewsfeed(getAccessToken(), startFrom)
                }
            nextFrom = responseDto.newsFeed.nextFrom
            val result = newsFeedMapper.mapResponseToPosts(responseDto)
            _feedPosts.addAll(result)
            emit(feedPosts)
        }
    }.retry(2) {
        true
    }.catch {
        Log.i("wallListFeedPost", it.message.toString())
    }

    private val tokenStatus = flow {
        currentTokenStatus.emit(Unit)
        currentTokenStatus.collect {
            val currentToken = token
            val isTokenValid = currentToken != null && currentToken.isValid
            val tokenResult = if (isTokenValid) LoginAppState.Success else LoginAppState.InProgress
            emit(tokenResult)
        }

    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = LoginAppState.InProgress
    )


    private val userFeedPosts: StateFlow<List<FeedPost>> = loadListFeedPost
        .mergeWith(refreshListFlow)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = feedPosts
        )

    override suspend fun checkAuthState() {
        currentTokenStatus.emit(Unit)
    }

    override suspend fun loadNextData() {
        nextDataNededEvent.emit(Unit)
    }

    override fun getAuthStateFlow() = tokenStatus

    override suspend fun changeLikeStatus(feedPost: FeedPost) {
        val response = if (feedPost.isLiked) {
            apiService.deleteLike(
                token = getAccessToken(),
                itemId = feedPost.postId,
                postId = feedPost.id,
            )
        } else {
            apiService.addLike(
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

    override suspend fun deletePost(feedPost: FeedPost) {
        val response = apiService.ignoreFeed(
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

    override fun getWallListFeedPost() = userFeedPosts

    override fun getFeedPostsComment(feedPost: FeedPost): StateFlow<CommentsState> = flow {
        val response = apiService.getComments(
            token = getAccessToken(),
            postId = feedPost.postId,
            ownerId = feedPost.id,
            extended = 1,
            fields = feedPost.userPhoto
        )
        emit(newsFeedMapper.mapResponseToComment(response))
    }
        .map { CommentsState.Success(comment = it) as CommentsState }
        .retry(2) { true }
        .catch { emit(CommentsState.Error(it)) }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = CommentsState.Initial
        )
}