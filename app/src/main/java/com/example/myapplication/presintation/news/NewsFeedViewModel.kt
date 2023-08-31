package com.example.myapplication.presintation.news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.NewsFeedRepository
import com.example.myapplication.domain.FeedPost
import com.example.myapplication.extention.mergeWith
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NewsFeedRepository(application = application)

    private val recommendationsFlow = repository.wallListFeedPost

    private val loadNextDataFlow = MutableSharedFlow<NewsFeedScreenState>()


    val screenState = recommendationsFlow
        .filter { it.isNotEmpty() }
        .map { NewsFeedScreenState.Post(it) as NewsFeedScreenState }
        .onStart { emit(NewsFeedScreenState.InProgress) }
        .mergeWith(loadNextDataFlow)


    fun loadNextNewsFeed() {
        viewModelScope.launch {
            loadNextDataFlow.emit(NewsFeedScreenState.Post(recommendationsFlow.value, true))
            repository.loadNextData()
        }

    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch {
            repository.changeLikeStatus(feedPost = feedPost)
        }
    }

    fun removeFeed(feedPost: FeedPost) {
        viewModelScope.launch {
            repository.deleteFeedFromList(feedPost)
        }
    }
}