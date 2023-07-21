package com.example.myapplication.navigation

import com.example.myapplication.domain.FeedPost
import com.google.gson.Gson

sealed class Screens(
    val root: String
) {
    object NewsFeed : Screens(HOME_NEWS_FEED)
    object Favorite : Screens(FAVORITE_SCREEN)
    object Profile : Screens(PROFILE_SCREEN)
    object Home : Screens(HOME_SCREEN)
    object Comments : Screens(HOME_COMMENTS) {

        private const val COMMENT_ARG = "comments"
        fun getCommentRrg(feedPost: FeedPost): String {
            val newPost = Gson().toJson(feedPost)
            return "$COMMENT_ARG/${newPost}"
        }
    }

    companion object {
        const val KEY_FEED_POST = "feed_post"

        private const val HOME_SCREEN = "home"
        private const val HOME_NEWS_FEED = "news_feed"
        private const val HOME_COMMENTS = "comments/{feed_post}"
        private const val FAVORITE_SCREEN = "favorite"
        private const val PROFILE_SCREEN = "profile"
    }
}
