package com.example.myapplication

import com.example.myapplication.domain.FeedPost
import com.example.myapplication.domain.PostComment

sealed class CommentScreenState {
    object Initial : CommentScreenState()
    class Comment(val feedPost: FeedPost, val comments: List<PostComment>) : CommentScreenState()
}
