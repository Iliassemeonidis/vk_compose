package com.example.myapplication.presintation.comment

import com.example.myapplication.domain.entity.FeedPost
import com.example.myapplication.domain.entity.PostComment

sealed class CommentScreenState {
    object Initial : CommentScreenState()
    object IsProgress : CommentScreenState()

    class Error(val message: Throwable) : CommentScreenState()
    class Comment(val feedPost: FeedPost, val comments: List<PostComment>) : CommentScreenState()

}
