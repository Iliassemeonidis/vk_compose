package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class FeedPostsDto(
    @SerializedName("source_id") val id: Int,
    @SerializedName("date") val publishDate: Long,
    @SerializedName("post_id") val post_id: Long,
    @SerializedName("comments") val comments: PostCommentDto,
    @SerializedName("attachments") val attachments: List<AttachmentsDto>?,
    @SerializedName("is_favorite") val isFavorite: Boolean,
    @SerializedName("likes") val likes: LikesDto,
    @SerializedName("reposts") val reposts: RepostsDto,
    @SerializedName("text") val text: String,
    @SerializedName("views") val views: ViewsDto,
)
