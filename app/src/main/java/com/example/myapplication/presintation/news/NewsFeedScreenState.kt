package com.example.myapplication.presintation.news

import com.example.myapplication.domain.FeedPost

sealed class NewsFeedScreenState {
    object Initial : NewsFeedScreenState()
    object InProgress : NewsFeedScreenState()


    class Post(val feedPosts: List<FeedPost>, val nextFrom : Boolean = false) : NewsFeedScreenState()
}
