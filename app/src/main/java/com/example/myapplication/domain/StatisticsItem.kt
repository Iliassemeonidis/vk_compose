package com.example.myapplication.domain

data class StatisticsItem(
    val type: StatisticsType,
    val count: Int = 0
)

enum class StatisticsType {
    VIEWS, SHARE, COMMENT, FAVORITE
}

