package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class CommentsDto(
    @SerializedName("id") val commentId : Long,
    @SerializedName("from_id") val authorCommentId : Int,
    @SerializedName("date") val publishDate : Long,
    @SerializedName("text") val commentText : String,
)
