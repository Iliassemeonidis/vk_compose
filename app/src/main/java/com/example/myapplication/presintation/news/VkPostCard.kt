package com.example.myapplication.presintation.news

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.domain.FeedPost
import com.example.myapplication.domain.StatisticsItem
import com.example.myapplication.domain.StatisticsType

data class ActionStatistic(
    val onViewItemClick: (FeedPost, StatisticsType) -> Unit,
    val onShearItemClick: (FeedPost, StatisticsType) -> Unit,
    val onFavoriteItemClick: (FeedPost, StatisticsType) -> Unit,
    val onItemRemove: (FeedPost) -> Unit
)

@Composable
fun PostCard(
    modifier: Modifier,
    feedPost: FeedPost,
    action: ActionStatistic,
    onCommentItemClick: (FeedPost) -> Unit
) {
    Card(modifier = modifier) {
        PostHeader(feedPost)

        Text(
            text = feedPost.contentText,
            modifier = Modifier.padding(8.dp)
        )

        AsyncImage(
            model = feedPost.contentImageUrl,
            modifier = Modifier.wrapContentWidth(),
            contentDescription = null,
        )

        Statistics(feedPost.statistics, action, feedPost, onCommentItemClick)

    }
}

@Composable
private fun Statistics(
    statistic: List<StatisticsItem>,
    action: ActionStatistic,
    feedPost: FeedPost,
    onCommentItemClick: (FeedPost) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(modifier = Modifier.weight(1f)) {
            val itemViews = statistic.getItemByType(StatisticsType.VIEWS)
            IconWithText(
                Icons.Rounded.Person,
                text = itemViews.count.toString(),
                onItemClickListener = { action.onViewItemClick(feedPost,itemViews.type) }
            )
        }
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.SpaceBetween) {
            val itemShare = statistic.getItemByType(StatisticsType.SHARE)
            IconWithText(
                Icons.Rounded.Share,
                itemShare.count.toString(),
                onItemClickListener = { action.onShearItemClick(feedPost, itemShare.type) }
            )

            val itemComment = statistic.getItemByType(StatisticsType.COMMENT)
            IconWithText(Icons.Rounded.Email, itemComment.count.toString(),
                onItemClickListener = {
                    onCommentItemClick(feedPost)
                }
            )

            val itemFavorite = statistic.getItemByType(StatisticsType.FAVORITE)
            IconWithText(Icons.Rounded.Favorite, itemFavorite.count.toString(),
                onItemClickListener = { action.onFavoriteItemClick(feedPost, itemFavorite.type) }
            )
        }
    }
}

private fun List<StatisticsItem>.getItemByType(type: StatisticsType): StatisticsItem {
    return this.find { it.type == type } ?: throw IllegalAccessException()
}


@Composable
private fun IconWithText(imageVector: ImageVector, text: String, onItemClickListener: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
        onItemClickListener()
    }) {
        Icon(imageVector = imageVector, contentDescription = "")
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, fontFamily = FontFamily.Cursive)
    }
}

@Composable
private fun PostHeader(feedPost: FeedPost) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model =  feedPost.profileUrl,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Yellow), contentDescription = null
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = feedPost.groupName)
            Spacer(modifier = Modifier.size(4.dp))
            Text(text = feedPost.publishDate, color = MaterialTheme.colorScheme.onSecondary)
        }
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
fun ActionAdditional(imageVector: ImageVector, text: String) {
    Icon(imageVector = imageVector, contentDescription = "")
    Text(text = text, fontFamily = FontFamily.Cursive)
}