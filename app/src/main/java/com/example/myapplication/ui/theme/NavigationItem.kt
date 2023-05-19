package com.example.myapplication.ui.theme

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myapplication.R

sealed class NavigationItem(@StringRes val titleName : Int, val imageVector: ImageVector ){

    object Home : NavigationItem(R.string.navigation_item_home, Icons.Outlined.Home)
    object Favorite : NavigationItem(R.string.navigation_item_favorite, Icons.Outlined.Favorite)
    object Profile : NavigationItem(R.string.navigation_item_profile, Icons.Outlined.Person)

}
