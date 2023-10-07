package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.repository.NewsFeedRepository
import javax.inject.Inject

class CheckAuthStatusUseCase @Inject constructor(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke() {
        repository.checkAuthState()
    }
}