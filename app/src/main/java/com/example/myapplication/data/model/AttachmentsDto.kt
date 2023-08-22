package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class AttachmentsDto(
    @SerializedName("photo") val photo: PhotoDto,
)