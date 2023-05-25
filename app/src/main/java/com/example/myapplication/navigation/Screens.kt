package com.example.myapplication.navigation

sealed class Screens(
    val root: String
) {
    object Home : Screens(HOME_SCREEN)
    object Favorite : Screens(FAVORITE_SCREEN)
    object Profile : Screens(PROFILE_SCREEN)

    companion object {
        private const val HOME_SCREEN = "home"
        private const val FAVORITE_SCREEN = "favorite"
        private const val PROFILE_SCREEN = "profile"
    }
}
