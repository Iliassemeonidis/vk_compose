package com.example.myapplication.presintation.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.FeedPost
import com.example.myapplication.domain.PostComment

class CommentViewModel(
    feedPost: FeedPost
) : ViewModel() {


    private val comments = mutableListOf<PostComment>().apply {
        repeat(10) {
            add(PostComment(id = it))
        }
    }




//    private val initialState = CommentScreenState.Comment(initial)
//
//    private var savedState: CommentScreenState? = initialState

    private var _screenSate = MutableLiveData<CommentScreenState>(CommentScreenState.Initial)
    val screenState: LiveData<CommentScreenState> = _screenSate

    init {
        loadComments(feedPost)
    }

    fun loadComments(feedPost: FeedPost) {
      //  savedState = _screenSate.value
        _screenSate.value = CommentScreenState.Comment(feedPost, comments)
    }

//    fun closeComment() {
//        _screenSate.value = savedState
//    }
}