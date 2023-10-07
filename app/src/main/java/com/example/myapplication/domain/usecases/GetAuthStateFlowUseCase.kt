package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.entity.FeedPost
import com.example.myapplication.domain.entity.LoginAppState
import com.example.myapplication.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetAuthStateFlowUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    operator fun invoke(): StateFlow<LoginAppState> {
        return repository.getAuthStateFlow()
    }
}