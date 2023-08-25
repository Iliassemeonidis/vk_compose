package com.example.myapplication.presintation.news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.NewsFeedRepository
import com.example.myapplication.domain.FeedPost
import com.example.myapplication.domain.StatisticsType
import kotlinx.coroutines.launch

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {
    private val initial = NewsFeedScreenState.Initial

    private val _screenSate = MutableLiveData<NewsFeedScreenState>(initial)
    val screenState: LiveData<NewsFeedScreenState> = _screenSate

    private val repository = NewsFeedRepository(application = application)

    init {
        _screenSate.value = NewsFeedScreenState.InProgress
        loadUserNewsFeed()
    }

    private fun loadUserNewsFeed() {
        viewModelScope.launch {
            val result = repository.loadFeedPost()
            _screenSate.value = NewsFeedScreenState.Post(result)
        }
    }

    fun loadNextNewsFeed() {
        _screenSate.value = NewsFeedScreenState.Post(repository.feedPost, true)
        loadUserNewsFeed()
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch {
            repository.changeLikeStatus(feedPost = feedPost)
            _screenSate.value = NewsFeedScreenState.Post(repository.feedPost)
        }
    }

    fun removeFeed(feedPost: FeedPost) {
        viewModelScope.launch {
            repository.deleteFeedFromList(feedPost)
            _screenSate.value = NewsFeedScreenState.Post(repository.feedPost)
        }
    }
}