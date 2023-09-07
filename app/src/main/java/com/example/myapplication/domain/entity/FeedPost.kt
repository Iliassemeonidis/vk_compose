package com.example.myapplication.domain.entity

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedPost(
    val id: Long,
    val postId: Long,
    val groupId : Int,
    val profileUrl: String,
    val groupName: String,
    val publishDate: String,
    val contentText: String,
    val contentImageUrl: String?,
    val isLiked : Boolean,
    val userPhoto : String = "photo_100",
    var statistics: List<StatisticsItem>


) : Parcelable {
    companion object {
        val NavTypeFeedPost: NavType<FeedPost> = object : NavType<FeedPost>(false) {

            override fun get(bundle: Bundle, key: String): FeedPost? {
                return bundle.getParcelable(key)
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
