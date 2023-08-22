package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class PhotoDto(
    @SerializedName("id") val id: Int,
    @SerializedName("date") val date: Long,
    @SerializedName("sizes") val image: List<PhotoUrlDto>
)
