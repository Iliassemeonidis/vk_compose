package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class ProfileDto(
    @SerializedName("id") val userId: Int,
    @SerializedName("photo_100") val userPhoto: String,
    @SerializedName("first_name") val firstName: String,
)
