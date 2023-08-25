package com.example.myapplication.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.myapplication.domain.FeedPost
import com.example.myapplication.navigation.Screens.Companion.KEY_FEED_POST
import com.google.gson.Gson
import java.lang.RuntimeException

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.homScreenNavGraph(
    homeNavigateDestination: @Composable () -> Unit,
    commentsNavigateDestination: @Composable (FeedPost) -> Unit,
) {
    navigation(
        startDestination = Screens.NewsFeed.root,
        route = Screens.Home.root
    ) {
        composable(Screens.NewsFeed.root) {
            homeNavigateDestination()
        }
        composable(route = "comments/{feed_post}",
            arguments = listOf(
                navArgument(KEY_FEED_POST) {
                    type = FeedPost.NavTypeFeedPost
                }
            )
        ) {
            val feedPost = it.arguments?.getParcelable<FeedPost>(KEY_FEED_POST)
                ?: throw RuntimeException("Args is null")
            commentsNavigateDestination(feedPost)
        }
    }
}