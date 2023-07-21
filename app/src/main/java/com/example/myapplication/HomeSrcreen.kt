package com.example.myapplication

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.domain.FeedPost
import com.example.myapplication.navigation.AppNavGraph
import com.example.myapplication.navigation.rememberNavigationState
import com.example.myapplication.navigation.NavigationItem

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewsScreen() {
    val viewModel: NewsFeedViewModel = viewModel()
    val listItem = listOf(NavigationItem.Home, NavigationItem.Favorite, NavigationItem.Profile)

    val navController = rememberNavigationState()


    val action = ActionStatistic(
        onViewItemClick = viewModel::updateFeedPostItem,
        onShearItemClick = viewModel::updateFeedPostItem,
        onFavoriteItemClick = viewModel::updateFeedPostItem,
        onItemRemove = viewModel::removeItem
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
                HomeScreen(action) {
                    navController.navigateToComment(it)
                }
            },
            commentsNavigateDestination = { feedPost ->
                CommentsScreen(
                    onBackPress = {
                        navController.navController.popBackStack()
                    },
                   feedPost =  feedPost
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
    onCommentItemClick: (FeedPost) -> Unit
) {
    val viewModel: NewsFeedViewModel = viewModel()
    val screenState = viewModel.screenState.observeAsState(NewsFeedScreenState.Initial)



    when (val currentState = screenState.value) {
        is NewsFeedScreenState.Post -> {
            FeedPosts(feedPosts = currentState.feedPosts, action, onCommentItemClick)
        }

        is NewsFeedScreenState.Initial -> {}

    }


}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
private fun FeedPosts(
    feedPosts: List<FeedPost>,
    action: ActionStatistic,
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
                            .background(Color.White),
                    )
                }
            )
        }
    }
}