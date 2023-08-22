package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class GroupDto(
    @SerializedName("id") val groupId: Int,
    @SerializedName("name") val groupName: String,
    @SerializedName("photo_200") val groupPhoto: String,
)
