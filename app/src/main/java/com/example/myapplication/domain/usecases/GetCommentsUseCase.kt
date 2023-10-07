package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.entity.FeedPost
import com.example.myapplication.domain.repository.NewsFeedRepository
import com.example.myapplication.presintation.comment.CommentsState
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
     operator fun invoke(feedPost: FeedPost): StateFlow<CommentsState> {
      return repository.getFeedPostsComment(feedPost)
    }
}