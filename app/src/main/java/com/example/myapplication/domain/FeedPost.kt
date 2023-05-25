package com.example.myapplication.domain

import com.example.myapplication.R

data class FeedPost(
    val id: Int = 0,
    val profileId: Int = R.drawable.ic_bart,
    val groupName: String = "Simpson Family",
    val publishDate: String = "14:00",
    val contentText: String = "Когда барт учился в школе он каждый раз оставался после уроков",
    val contentImage: Int = R.drawable.ic_shcool,
    var statistics: List<StatisticsItem> = listOf(
        StatisticsItem(StatisticsType.VIEWS, count = 260),
        StatisticsItem(StatisticsType.SHARE, count = 20),
        StatisticsItem(StatisticsType.COMMENT, count = 260),
        StatisticsItem(StatisticsType.FAVORITE, count = 60),
    )
)
