package com.example.myapplication

import com.example.myapplication.domain.FeedPost

sealed class NewsFeedScreenState {
    object Initial : NewsFeedScreenState()
    class Post(val feedPosts: List<FeedPost>) : NewsFeedScreenState()
}
