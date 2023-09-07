package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.repository.NewsFeedRepository

class CheckAuthStatusUseCase(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke() {
        repository.checkAuthState()
    }
}