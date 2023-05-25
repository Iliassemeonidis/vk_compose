package com.example.myapplication.domain

import com.example.myapplication.R

data class FeedComments(
    val id : Int = 0,
    val iconId : Int = R.drawable.ic_man,
    val authorName : String = "User name WTF",
    val comment : String = "Long comment some niger",
    val publishDate :String = "12:00"
)