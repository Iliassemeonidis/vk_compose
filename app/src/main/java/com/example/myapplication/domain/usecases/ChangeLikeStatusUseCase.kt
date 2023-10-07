package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.entity.FeedPost
import com.example.myapplication.domain.repository.NewsFeedRepository
import javax.inject.Inject

class ChangeLikeStatusUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke(feedPost: FeedPost) {
        repository.changeLikeStatus(feedPost)
    }
}