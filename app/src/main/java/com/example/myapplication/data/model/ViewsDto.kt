package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class ViewsDto(
    @SerializedName("views") val count: Int
)
