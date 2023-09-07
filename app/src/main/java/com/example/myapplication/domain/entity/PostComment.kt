package com.example.myapplication.domain.entity

data class PostComment(
    val id : Long ,
    val iconId : String ,
    val authorName : String ,
    val comment : String ,
    val publishDate :String
)