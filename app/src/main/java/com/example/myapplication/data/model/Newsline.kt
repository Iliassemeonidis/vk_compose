package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class NewsLine(
    @SerializedName("items") val items: List<FeedPostsDto>,
    @SerializedName("profiles") val profile: List<ProfileDto>,
    @SerializedName("groups") val groups: List<GroupDto>,
)
