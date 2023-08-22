package com.example.myapplication.presintation.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthenticationResult

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private var _authState = MutableLiveData<LoginAppState>(LoginAppState.InProgress)
    val authState: LiveData<LoginAppState> = _authState

    init {
        val keyValueStorage = VKPreferencesKeyValueStorage(application)
        val token = VKAccessToken.restore(keyValueStorage)
        val isTokenValid = token != null && token.isValid
        _authState.value = if (isTokenValid) LoginAppState.Success
            else LoginAppState.InProgress

        if(isTokenValid) Log.i("USER_TOKEN", "${token?.accessToken}")

    }

    fun checkUserLogin(result: VKAuthenticationResult) {
        if (result is VKAuthenticationResult.Success) {
            _authState.value = LoginAppState.Success
        } else {
            _authState.value = LoginAppState.Failure
        }
    }
}
