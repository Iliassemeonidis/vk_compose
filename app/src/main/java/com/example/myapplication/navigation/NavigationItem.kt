package com.example.myapplication.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myapplication.R

sealed class NavigationItem(
    @StringRes val titleName: Int,
    val imageVector: ImageVector,
    val screens: Screens
) {

    object Home : NavigationItem(
        R.string.navigation_item_home,
        Icons.Outlined.Home,
        screens = Screens.NewsFeed
    )

    object Favorite : NavigationItem(
        R.string.navigation_item_favorite,
        Icons.Outlined.Favorite,
        screens = Screens.Favorite
    )

    object Profile :
        NavigationItem(
            R.string.navigation_item_profile, Icons.Outlined.Person,
            screens = Screens.Profile
        )

}
