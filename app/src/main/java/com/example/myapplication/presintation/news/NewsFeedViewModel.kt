package com.example.myapplication.presintation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.entity.FeedPost
import com.example.myapplication.domain.usecases.ChangeLikeStatusUseCase
import com.example.myapplication.domain.usecases.DeletePostUseCase
import com.example.myapplication.domain.usecases.GetWallFeedPostUseCase
import com.example.myapplication.domain.usecases.LoadNextDataUseCase
import com.example.myapplication.extention.mergeWith
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsFeedViewModel @Inject constructor(
    private val getWallFeedPostUseCase: GetWallFeedPostUseCase,
    private val loadNextDataUseCase: LoadNextDataUseCase,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase,
    private val deletePostUseCase: DeletePostUseCase
) : ViewModel() {

    private val recommendationsFlow = getWallFeedPostUseCase()

    private val loadNextDataFlow = MutableSharedFlow<NewsFeedState>()


    val screenState = recommendationsFlow
        .filter { it.isNotEmpty() }
        .map { NewsFeedState.Post(it) as NewsFeedState }
        .onStart { emit(NewsFeedState.InProgress) }
        .mergeWith(loadNextDataFlow)


    fun loadNextNewsFeed() {
        viewModelScope.launch {
            loadNextDataFlow
                .emit(
                    NewsFeedState.Post(
                        feedPosts = recommendationsFlow.value,
                        nextFrom = true
                    )
                )
            loadNextDataUseCase()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch {
            changeLikeStatusUseCase(feedPost = feedPost)
        }
    }

    fun removeFeed(feedPost: FeedPost) {
        viewModelScope.launch {
            deletePostUseCase(feedPost)
        }
    }
}