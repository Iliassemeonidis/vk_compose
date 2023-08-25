package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class NewsFeed(
    @SerializedName("items") val items: List<FeedPostsDto>,
    @SerializedName("profiles") val profile: List<ProfileDto>,
    @SerializedName("groups") val groups: List<GroupDto>,
    @SerializedName("next_from") val nextFrom: String?,
)
