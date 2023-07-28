package com.example.myapplication.presintation.main

sealed class LoginAppState{
    object InProgress : LoginAppState()
    object Success : LoginAppState()
    object Failure : LoginAppState()
}
