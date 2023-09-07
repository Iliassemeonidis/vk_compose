package com.example.myapplication.presintation.main

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.domain.entity.FeedPost
import com.example.myapplication.navigation.AppNavGraph
import com.example.myapplication.navigation.NavigationItem
import com.example.myapplication.navigation.rememberNavigationState
import com.example.myapplication.presintation.comment.CommentsScreen
import com.example.myapplication.presintation.news.ActionStatistic
import com.example.myapplication.presintation.news.NewsFeedState
import com.example.myapplication.presintation.news.NewsFeedViewModel
import com.example.myapplication.presintation.news.PostCard

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewsScreen() {
    val viewModel: NewsFeedViewModel = viewModel()
    val screenState = viewModel.screenState.collectAsState(NewsFeedState.Initial)
    val listItem = listOf(NavigationItem.Home, NavigationItem.Favorite, NavigationItem.Profile)
    val navController = rememberNavigationState()


    val action = ActionStatistic(
        onChangeLikeStatus = viewModel::changeLikeStatus,
        onItemRemove = viewModel::removeFeed,
        onLoadNextFeed = viewModel::loadNextNewsFeed
    )

    Scaffold(
        bottomBar = {
            NavigationBar {

                val backStack by navController.navController.currentBackStackEntryAsState()

                listItem.forEach { item ->

                    val selected = backStack?.destination?.hierarchy?.any {
                        it.route == item.screens.root
                    } ?: false

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                navController.navigateTo(item.screens.root)
                            }
                        },
                        icon = { Icon(item.imageVector, contentDescription = null) },
                        label = { Text(text = stringResource(id = item.titleName)) }
                    )
                }
            }
        }
    ) {
        AppNavGraph(
            navController = navController.navController,
            newsFeedNavigateDestination = {
                HomeScreen(action, screenState) {
                    navController.navigateToComment(it)
                }

            },
            commentsNavigateDestination = { feedPost ->
                CommentsScreen(
                    onBackPress = {
                        navController.navController.popBackStack()
                    },
                    feedPost = feedPost
                )
            },
            favoriteNavigateDestination = { NewScreen("Favorite") },
            profileNavigateDestination = { NewScreen("Profile") }
        )

    }
}

@Composable
fun NewScreen(text: String) {
    var counter by rememberSaveable {
        mutableStateOf(0)
    }

    Text(text = "$text count $counter",
        color = Color.DarkGray,
        modifier = Modifier
            .padding(8.dp)
            .clickable { counter++ }
    )

}

@Composable
private fun HomeScreen(
    action: ActionStatistic,
    screenState: State<NewsFeedState?>,
    onCommentItemClick: (FeedPost) -> Unit
) {
    when (val result = screenState.value) {
        is NewsFeedState.Post -> {
            FeedPosts(
                feedPosts = result.feedPosts,
                action,
                isNextFrom = result.nextFrom,
                onCommentItemClick
            )
        }

        is NewsFeedState.Initial -> {
        }

        is NewsFeedState.InProgress -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CustomCircularProgressBar()
            }
        }

        else -> {}
    }
}

@Composable
private fun CustomCircularProgressBar() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(start = 150.dp, top = 250.dp)
                .size(100.dp),
            color = Color.Black,
            strokeWidth = 10.dp
        )
    }


}

@Composable
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
private fun FeedPosts(
    feedPosts: List<FeedPost>,
    action: ActionStatistic,
    isNextFrom: Boolean,
    onCommentItemClick: (FeedPost) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(bottom = 100.dp)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(feedPosts, key = { it.id }) { model ->
            val dismiss = rememberDismissState()

            if (dismiss.isDismissed(DismissDirection.EndToStart)) {
                action.onItemRemove(model)
            }

            SwipeToDismiss(
                modifier = Modifier.animateItemPlacement(),
                state = dismiss,
                directions = setOf(DismissDirection.EndToStart),
                dismissContent = {
                    PostCard(
                        Modifier,
                        feedPost = model,
                        action = action
                    ) { onCommentItemClick(it) }
                },
                background = {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                            .background(Color.Blue.copy(alpha = 1f)),
                    )
                }
            )
        }

        item {
            if (isNextFrom) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Blue)
                }
            } else {
                SideEffect {
                    action.onLoadNextFeed()

                }
            }
        }
    }
}