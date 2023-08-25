package com.example.myapplication.domain

import com.example.myapplication.R

data class PostComment(
    val id : Long ,
    val iconId : String ,
    val authorName : String ,
    val comment : String ,
    val publishDate :String
)