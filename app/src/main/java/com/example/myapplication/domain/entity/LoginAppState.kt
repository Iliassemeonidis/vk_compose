package com.example.myapplication.domain.entity

sealed class LoginAppState{
    object InProgress : LoginAppState()
    object Success : LoginAppState()
    object Failure : LoginAppState()
}
