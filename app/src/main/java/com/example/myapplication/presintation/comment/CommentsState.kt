package com.example.myapplication.presintation.comment

import com.example.myapplication.domain.entity.PostComment

sealed class CommentsState {
    object Initial : CommentsState()
    class Success(val comment: List<PostComment>) : CommentsState()
    class Error(val m: Throwable) : CommentsState()

}