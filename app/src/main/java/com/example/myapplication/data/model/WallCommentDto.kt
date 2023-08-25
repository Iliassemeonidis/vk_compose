package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class WallCommentDto(
    @SerializedName("items") val items : List<CommentsDto>,
    @SerializedName("profiles") val profiles : List<UserProfileDto>
)
