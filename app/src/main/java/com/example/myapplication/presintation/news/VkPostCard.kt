package com.example.myapplication.presintation.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.domain.entity.FeedPost
import com.example.myapplication.domain.entity.StatisticsItem
import com.example.myapplication.domain.entity.StatisticsType
import com.example.myapplication.ui.theme.DarkRed

data class ActionStatistic(
    val onChangeLikeStatus: (FeedPost) -> Unit,
    val onItemRemove: (FeedPost) -> Unit,
    val onLoadNextFeed: () -> Unit
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
            maxLines = 5,

            overflow = TextOverflow.Ellipsis,
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
                R.drawable.ic_views_count,
                count = itemViews.count,
                onItemClickListener = {
                }
            )
        }
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.SpaceBetween) {
            val itemShare = statistic.getItemByType(StatisticsType.SHARE)
            IconWithText(
                R.drawable.ic_share,
                itemShare.count,
            )

            val itemComment = statistic.getItemByType(StatisticsType.COMMENT)
            IconWithText(R.drawable.ic_comment, itemComment.count,
                onItemClickListener = {
                    onCommentItemClick(feedPost)
                }
            )

            val itemFavorite = statistic.getItemByType(StatisticsType.FAVORITE)
            IconWithText(
                if (feedPost.isLiked) R.drawable.ic_like_set else R.drawable.ic_like,
                itemFavorite.count,
                tint = if (feedPost.isLiked) DarkRed else MaterialTheme.colorScheme.onSecondary,
                onItemClickListener = { action.onChangeLikeStatus(feedPost) }
            )
        }
    }
}

private fun List<StatisticsItem>.getItemByType(type: StatisticsType): StatisticsItem {
    return this.find { it.type == type } ?: throw IllegalAccessException()
}


@Composable
private fun IconWithText(
    imageVector: Int,
    count: Int,
    tint: Color = Color.Black,
    onItemClickListener: (() -> Unit)? = null
) {
    val modifier = if (onItemClickListener == null) {
        Modifier
    } else {
        Modifier.clickable {
            onItemClickListener()
        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = imageVector),
            contentDescription = "",
            tint = tint
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = formatStatisticCount(count),
            fontFamily = FontFamily.Cursive
        )
    }
}

private fun formatStatisticCount(count: Int): String {
    return if (count > 100_000) {
        String.format("%sK", (count / 1000))
    } else if (count > 1000) {
        String.format("%.1f", (count / 1000f))
    } else {
        count.toString()
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
            model = feedPost.profileUrl,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentDescription = null
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = feedPost.groupName)
            Spacer(modifier = Modifier.size(4.dp))
            Text(text = feedPost.publishDate, color = Color.Black)
        }
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = null,
            tint = Color.Black
        )
    }
}

@Composable
fun ActionAdditional(imageVector: ImageVector, text: String) {
    Icon(imageVector = imageVector, contentDescription = "")
    Text(text = text, fontFamily = FontFamily.Cursive)
}