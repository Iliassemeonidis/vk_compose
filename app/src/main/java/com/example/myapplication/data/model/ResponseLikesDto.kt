package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class ResponseLikesDto(
    @SerializedName("response") val likes : LikesCountDto
)
