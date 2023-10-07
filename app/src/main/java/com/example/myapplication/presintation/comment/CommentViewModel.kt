package com.example.myapplication.presintation.comment

import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.entity.FeedPost
import com.example.myapplication.domain.usecases.GetCommentsUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class CommentViewModel @Inject constructor(
    private val feedPost: FeedPost,
    private val getCommentsUseCase: GetCommentsUseCase
) : ViewModel() {

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