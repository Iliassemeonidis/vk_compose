package com.example.myapplication.presintation.news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.NewsFeedRepositoryImpl
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

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NewsFeedRepositoryImpl(application = application)

    val getWallFeedPostUseCase = GetWallFeedPostUseCase(repository)

    val loadNextDataUseCase = LoadNextDataUseCase(repository)
    val changeLikeStatusUseCase = ChangeLikeStatusUseCase(repository)
    val deletePostUseCase = DeletePostUseCase(repository)

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
            ))
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