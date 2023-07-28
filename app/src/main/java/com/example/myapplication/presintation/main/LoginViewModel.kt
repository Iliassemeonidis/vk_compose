package com.example.myapplication.presintation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult

class LoginViewModel() : ViewModel() {

    private var _authState = MutableLiveData<LoginAppState>(LoginAppState.InProgress)
    val authState: LiveData<LoginAppState> = _authState

    init {
        _authState.value =
            if (VK.isLoggedIn()) LoginAppState.Success
            else LoginAppState.InProgress
    }

    fun checkUserLogin(result: VKAuthenticationResult) {
        if (result is VKAuthenticationResult.Success) {
            _authState.value = LoginAppState.Success
        } else {
            _authState.value = LoginAppState.Failure
        }
    }
}
