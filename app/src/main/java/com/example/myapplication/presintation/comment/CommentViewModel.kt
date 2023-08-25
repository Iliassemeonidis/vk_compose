package com.example.myapplication.presintation.comment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.NewsFeedRepository
import com.example.myapplication.domain.FeedPost
import kotlinx.coroutines.launch

class CommentViewModel(
    feedPost: FeedPost,
    application: Context
) : ViewModel() {

    private var _screenSate = MutableLiveData<CommentScreenState>(CommentScreenState.Initial)
    val screenState: LiveData<CommentScreenState> = _screenSate

    private val repository = NewsFeedRepository(application = application)

    init {
        _screenSate.value = CommentScreenState.IsProgress
        loadComments(feedPost)
    }

    private fun loadComments(feedPost: FeedPost) {
        viewModelScope.launch {
            val result = repository.getFeedPostsComment(feedPost)
            _screenSate.value = CommentScreenState.Comment(feedPost, result)
        }
    }

}