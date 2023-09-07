package com.example.myapplication.presintation.news

import com.example.myapplication.domain.entity.FeedPost

sealed class NewsFeedState {
    object Initial : NewsFeedState()
    object InProgress : NewsFeedState()


    class Post(val feedPosts: List<FeedPost>, val nextFrom : Boolean = false) : NewsFeedState()
}
