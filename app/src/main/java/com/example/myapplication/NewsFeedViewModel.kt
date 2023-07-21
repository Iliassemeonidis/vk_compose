package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.FeedPost
import com.example.myapplication.domain.StatisticsType

class NewsFeedViewModel() : ViewModel() {

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



    private val initialState = NewsFeedScreenState.Post(initial)

    private var _screenSate = MutableLiveData<NewsFeedScreenState>(initialState)
    val screenState: LiveData<NewsFeedScreenState> = _screenSate


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