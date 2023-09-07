package com.example.myapplication.presintation.comment

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.repository.NewsFeedRepositoryImpl
import com.example.myapplication.domain.entity.FeedPost
import com.example.myapplication.domain.usecases.GetCommentsUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class CommentViewModel(
    feedPost: FeedPost,
    application: Context
) : ViewModel() {

    private val repository = NewsFeedRepositoryImpl(application = application)

    private val getCommentsUseCase = GetCommentsUseCase(repository)

    val screenState = getCommentsUseCase(feedPost)
        .map {
            when (it) {
                is CommentsState.Error -> {
                    CommentScreenState.Error(it.m)
                }

                is CommentsState.Success -> {
                    CommentScreenState.Comment(
                        feedPost = feedPost,
                        it.comment
                    ) as CommentScreenState
                }

                CommentsState.Initial -> {}
            }
        }
        .onStart { emit(CommentScreenState.IsProgress) }


}