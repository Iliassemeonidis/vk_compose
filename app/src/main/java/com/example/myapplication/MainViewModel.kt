package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.FeedPost
import com.example.myapplication.domain.StatisticsType

class MainViewModel() : ViewModel() {

    private var _feedPost = MutableLiveData(FeedPost())
    val feedPost: LiveData<FeedPost> = _feedPost


    fun updateFeedPostItem(item: StatisticsType) {
        val oldFeedPost = _feedPost.value?.statistics ?: throw IllegalAccessException()
        val newFeedPost = oldFeedPost.toMutableList().apply {
            replaceAll { oldItem ->
                if (oldItem.type == item) {
                    oldItem.copy(count = oldItem.count + 1)
                } else {
                    oldItem
                }
            }

        }
        _feedPost.value = _feedPost.value?.copy(statistics = newFeedPost)
    }

}