package com.example.myapplication.presintation.news

import com.example.myapplication.domain.FeedPost

sealed class NewsFeedScreenState {
    object Initial : NewsFeedScreenState()
    class Post(val feedPosts: List<FeedPost>) : NewsFeedScreenState()
}
