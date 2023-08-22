package com.example.myapplication.presintation.news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.mapper.NewsFeedMapper
import com.example.myapplication.data.network.ApiFactory
import com.example.myapplication.domain.FeedPost
import com.example.myapplication.domain.StatisticsType
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.launch

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {
    private val initial = NewsFeedScreenState.Initial

    private val _screenSate = MutableLiveData<NewsFeedScreenState>(initial)
    val screenState: LiveData<NewsFeedScreenState> = _screenSate

    init {
        loadUserNewsFeed(application)
    }

    private fun loadUserNewsFeed(application: Application) {
        viewModelScope.launch {
            val keyValueStorage = VKPreferencesKeyValueStorage(application)
            val token = VKAccessToken.restore(keyValueStorage) ?: return@launch
            val responseDto = ApiFactory.apiService.loadUserNewsfeed(token.accessToken)
            val feedPosts = NewsFeedMapper.mapResponseToPosts(responseDto)

            _screenSate.value = NewsFeedScreenState.Post(feedPosts)
        }
    }


    fun updateFeedPostItem(feedPost: FeedPost, item: StatisticsType) {
        val currentSate = screenState.value
        if (currentSate !is NewsFeedScreenState.Post) return

        // get old List<FeedPost>
        val oldFeedPost = currentSate.feedPosts.toMutableList()

        // get statistics from clicked card from FeedPost
        val oldFeedStatics = feedPost.statistics

        //  convert taked oldFeedStatics to MutableList
        val newStatics = oldFeedStatics.toMutableList()

        // check equals type and replace counter
        newStatics.apply {
            replaceAll {
                if (it.type == item) {
                    it.copy(count = it.count + 1)
                } else
                    it
            }
        }


        // crate new object FeedPost
        val newFeedPost = feedPost.copy(statistics = newStatics)

        // take old feed post find changed feed post and replace it and then return new list feed post
        val newPost = oldFeedPost.apply {
            replaceAll {
                if (it.id == newFeedPost.id) {
                    newFeedPost
                } else {
                    it
                }
            }
        }
        _screenSate.value = NewsFeedScreenState.Post(newPost)

    }

    fun removeItem(item: FeedPost) {
        val currentSate = screenState.value
        if (currentSate !is NewsFeedScreenState.Post) return

        val newListItem = currentSate.feedPosts.toMutableList()
        newListItem.remove(item)
        _screenSate.value = NewsFeedScreenState.Post(newListItem)
    }
}