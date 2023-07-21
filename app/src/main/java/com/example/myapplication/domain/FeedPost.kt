package com.example.myapplication.domain

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import com.example.myapplication.R
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedPost(
    val id: Int = 0,
    val profileId: Int = R.drawable.ic_bart,
    val groupName: String = "Simpson Family",
    val publishDate: String = "14:00",
    val contentText: String = "Когда барт учился в школе он каждый раз оставался после уроков",
    val contentImage: Int = R.drawable.ic_shcool,
    var statistics: List<StatisticsItem> = listOf(
        StatisticsItem(StatisticsType.VIEWS, count = 260),
        StatisticsItem(StatisticsType.SHARE, count = 20),
        StatisticsItem(StatisticsType.COMMENT, count = 260),
        StatisticsItem(StatisticsType.FAVORITE, count = 60),
    )


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
