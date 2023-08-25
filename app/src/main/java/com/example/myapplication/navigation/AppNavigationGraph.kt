package com.example.myapplication.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.domain.FeedPost

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    favoriteNavigateDestination: @Composable () -> Unit,
    profileNavigateDestination: @Composable () -> Unit,
    newsFeedNavigateDestination: @Composable () -> Unit,
    commentsNavigateDestination: @Composable (FeedPost) -> Unit,
) {

    NavHost(
        navController = navController,
        startDestination = Screens.Home.root
    ) {
        homScreenNavGraph(
            newsFeedNavigateDestination,
            commentsNavigateDestination
        )
        composable(Screens.Favorite.root) {
            favoriteNavigateDestination()
        }
        composable(Screens.Profile.root) {
            profileNavigateDestination()
        }
    }
}