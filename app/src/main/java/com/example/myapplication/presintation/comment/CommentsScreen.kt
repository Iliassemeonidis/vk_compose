package com.example.myapplication.presintation.comment

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.domain.entity.FeedPost
import com.example.myapplication.presintation.NewsFeedApplication

@Composable
fun CommentsScreen(
    onBackPress: () -> Unit,
    feedPost: FeedPost,
) {

    val component = (LocalContext.current.applicationContext as NewsFeedApplication).component
        .getCommentsScreenComponentFactory()
        .create(feedPost)
        .getViewModelFactory()

    val viewModel: CommentViewModel = viewModel(factory = component)
    val screenState = viewModel.screenState.collectAsState(CommentScreenState.Initial)

    when (val currentState = screenState.value) {
        is CommentScreenState.Comment -> {
            if (currentState.comments.isEmpty()) {
                EmptyComment(onBackPress)
            } else {
                Comment(currentState, onBackPress)
            }
        }

        CommentScreenState.Initial -> {}

        CommentScreenState.IsProgress -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Blue)
            }
        }

        is CommentScreenState.Error -> {
            Toast.makeText(LocalContext.current, currentState.message.toString() , Toast.LENGTH_SHORT).show()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmptyComment(onBackPress: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.comment))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPress()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null
                        )
                    }
                })
        },
        content = { padingVla ->

            Box(
                modifier = Modifier
                    .padding(padingVla)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    modifier = Modifier.padding(top = 30.dp),
                    text = stringResource(R.string.has_not_comment),
                    fontSize = 20.sp
                )
            }
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Comment(
    currentState: CommentScreenState.Comment,
    onBackPress: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.comment))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPress()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null
                        )
                    }
                })
        },
        content = { padingVla ->
            LazyColumn(
                modifier = Modifier.padding(padingVla),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 72.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = currentState.comments, key = { it.id }) {
                    Row {
                        AsyncImage(
                            model = it.iconId,
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                modifier = Modifier.padding(start = 10.dp),
                                text = it.authorName,
                                color = Color.DarkGray,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                modifier = Modifier.padding(start = 10.dp),
                                text = it.comment,
                                color = Color.DarkGray,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                modifier = Modifier.padding(10.dp),
                                text = it.publishDate,
                                color = Color.DarkGray,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
            //todo impl upload comments
//            item {
//                if (isNextFrom) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .wrapContentSize()
//                            .padding(16.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        CircularProgressIndicator(color = Color.Blue)
//                    }
//                } else {
//                    SideEffect {
//                        action.onLoadNextFeed()
//                    }
//                }
//            }
        }
    )
}