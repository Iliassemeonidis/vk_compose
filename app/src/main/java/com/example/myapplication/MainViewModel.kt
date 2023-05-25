package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.FeedPost
import com.example.myapplication.domain.StatisticsType
import com.example.myapplication.ui.theme.NavigationItem

class MainViewModel() : ViewModel() {

    val initial = mutableListOf<FeedPost>().apply {
        repeat(5) {
            add(
                FeedPost(
                    id = it,
                    groupName = "Simpson Family $it"
                )
            )
        }
    }


    private var _feedPosts = MutableLiveData<List<FeedPost>>(initial)
    val feedPosts: LiveData<List<FeedPost>> = _feedPosts


    fun updateFeedPostItem(feedPost: FeedPost, item: StatisticsType) {
        // get old List<FeedPost>
        val oldFeedPost = feedPosts.value?.toMutableList() ?: mutableListOf()

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
        _feedPosts.value = oldFeedPost.apply {
            replaceAll {
                if (it.id == newFeedPost.id) {
                    newFeedPost
                } else {
                    it
                }
            }
        }
    }

    fun removeItem(item: FeedPost) {
        val newListItem = _feedPosts.value?.toMutableList() ?: mutableListOf()
        newListItem.remove(item)
        _feedPosts.value = newListItem
    }
}