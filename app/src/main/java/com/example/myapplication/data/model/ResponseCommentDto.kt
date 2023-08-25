package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class ResponseCommentDto(
    @SerializedName("response") val comment: WallCommentDto
)
