package com.example.myapplication.domain

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedPost(
    val id: Int = 0,
    val profileUrl: String,
    val groupName: String,
    val publishDate: String,
    val contentText: String,
    val contentImageUrl: String?,
    var statistics: List<StatisticsItem>


) : Parcelable {
    companion object {
        val NavTypeFeedPost: NavType<FeedPost> = object : NavType<FeedPost>(false) {

            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun get(bundle: Bundle, key: String): FeedPost? {
                return bundle.getParcelable(key, FeedPost::class.java)
            }

            override fun parseValue(value: String): FeedPost {
                return Gson().fromJson(value, FeedPost::class.java)
            }

            override fun put(bundle: Bundle, key: String, value: FeedPost) {
                bundle.putParcelable(key, value)
            }

        }
    }

}
