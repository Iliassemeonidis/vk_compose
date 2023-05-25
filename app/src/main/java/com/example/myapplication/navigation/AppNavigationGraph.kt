package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    navController: NavHostController,
    homeNavigateDestination: @Composable () -> Unit,
    favoriteNavigateDestination: @Composable () -> Unit,
    profileNavigateDestination: @Composable () -> Unit,
) {

    NavHost(
        navController = navController,
        startDestination = Screens.Home.root
    ) {
        composable(Screens.Home.root) {
            homeNavigateDestination()
        }
        composable(Screens.Favorite.root) {
            favoriteNavigateDestination()
        }
        composable(Screens.Profile.root) {
            profileNavigateDestination()
        }
    }
}