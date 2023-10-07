package com.example.myapplication.presintation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecases.CheckAuthStatusUseCase
import com.example.myapplication.domain.usecases.GetAuthStateFlowUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    val checkAuthStatusUseCase: CheckAuthStatusUseCase,
    val getAuthStateFlowUseCase: GetAuthStateFlowUseCase,
) : ViewModel() {

    val authState = getAuthStateFlowUseCase()

    fun checkUserLogin() {
        viewModelScope.launch {
            checkAuthStatusUseCase()
        }
    }
}
