package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class UserProfileDto(
    @SerializedName("id") val userId: Int,
    @SerializedName("photo_100") val userPhoto: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
)
