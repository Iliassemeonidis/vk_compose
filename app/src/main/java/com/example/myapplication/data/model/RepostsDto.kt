package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class RepostsDto(
    @SerializedName("reposts") val count: Int
)
