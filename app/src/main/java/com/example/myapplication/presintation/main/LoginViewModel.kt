package com.example.myapplication.presintation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.NewsFeedRepositoryImpl
import com.example.myapplication.domain.usecases.CheckAuthStatusUseCase
import com.example.myapplication.domain.usecases.GetAuthStateFlowUseCase
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NewsFeedRepositoryImpl(application)

    val checkAuthStatusUseCase = CheckAuthStatusUseCase(repository)

    val getAuthStateFlowUseCase = GetAuthStateFlowUseCase(repository)

    val authState = getAuthStateFlowUseCase()


    fun checkUserLogin() {
        viewModelScope.launch {
            checkAuthStatusUseCase()
        }
    }
}
