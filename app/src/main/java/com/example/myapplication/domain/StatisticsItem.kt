package com.example.myapplication.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatisticsItem(
    val type: StatisticsType,
    val count: Int = 0
) : Parcelable

enum class StatisticsType {
    VIEWS, SHARE, COMMENT, FAVORITE
}

