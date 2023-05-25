package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.domain.FeedComments
import com.example.myapplication.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Comments(feedComments: List<FeedComments>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Commets for FeedPost id : 0")
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
                    }
                })
        },
        content = { padingVla ->
            LazyColumn(modifier = Modifier.padding(padingVla),
                contentPadding = PaddingValues(
                    top = 10.dp,
                    end = 8.dp,
                    start = 10.dp,
                    bottom = 72.dp
                )
            ) {
                items(items = feedComments, key = { it.id }) {
                    Row {
                        Image(painter = painterResource(id = it.iconId), contentDescription = null, modifier = Modifier.size(50.dp))
                        Column {
                            Text(
                                text = it.authorName,
                                color = Color.DarkGray,
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = it.comment,
                                color = Color.DarkGray,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = it.publishDate,
                                color = Color.DarkGray,
                                fontSize = 12.sp
                            )
                        }

                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun CommentPreview() {
    MyApplicationTheme() {
        val ite = mutableListOf<FeedComments>().apply {
            repeat(10) {
                add(
                    FeedComments(id = it)
                )
            }

        }
        Comments(ite)
    }
}