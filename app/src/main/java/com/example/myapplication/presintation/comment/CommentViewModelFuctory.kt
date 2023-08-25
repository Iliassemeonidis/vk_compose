package com.example.myapplication.presintation.comment

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.domain.FeedPost

class CommentViewModelFactory(
    private val feedPost: FeedPost,
    private val application: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentViewModel(application = application, feedPost = feedPost) as T
    }
}