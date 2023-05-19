package com.example.myapplication

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.domain.FeedPost
import com.example.myapplication.ui.theme.NavigationItem
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(viewModel : MainViewModel) {

    val listItem = listOf(NavigationItem.Home, NavigationItem.Favorite, NavigationItem.Profile)
    val itemSelected = remember { mutableStateOf(0) }

    val mutableFeedPost = viewModel.feedPost.observeAsState(FeedPost())

    val action = ActionStatistic (
        onViewItemClick = viewModel::updateFeedPostItem,
        onShearItemClick = viewModel::updateFeedPostItem,
        onCommentItemClick = viewModel::updateFeedPostItem,
        onFavoriteItemClick = viewModel::updateFeedPostItem
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                listItem.forEachIndexed { index, navigationItem ->
                    NavigationBarItem(
                        selected = itemSelected.value == index,
                        onClick = { itemSelected.value = index },
                        icon = { Icon(navigationItem.imageVector, contentDescription = null) },
                        label = { Text(text = stringResource(id = navigationItem.titleName)) }
                    )
                }
            }
        }
    ) {
        PostCard(
            modifier = Modifier.padding(8.dp),
            feedPost = mutableFeedPost.value,
            action = action
        )
    }
}
