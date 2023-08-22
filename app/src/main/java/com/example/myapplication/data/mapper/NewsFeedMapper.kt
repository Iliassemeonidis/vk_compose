package com.example.myapplication.data.mapper

import com.example.myapplication.data.model.NewsFeedResponseDto
import com.example.myapplication.domain.FeedPost
import com.example.myapplication.domain.StatisticsItem
import com.example.myapplication.domain.StatisticsType
import kotlin.math.absoluteValue

object NewsFeedMapper {

    fun mapResponseToPosts(newsFeedResponseDto: NewsFeedResponseDto): List<FeedPost> {
        val mapperResultFeedPost = mutableListOf<FeedPost>()

        val items = newsFeedResponseDto.newsLine.items
        val groups = newsFeedResponseDto.newsLine.groups

        items.forEach { item ->
            val userGroup =
                groups.find { it.groupId.absoluteValue == item.id.absoluteValue } ?: return@forEach

            if (item.attachments == null) return@forEach
            val feedPost = FeedPost(
                id = item.id,
                profileUrl = userGroup.groupPhoto,
                groupName = userGroup.groupName,
                publishDate = item.publishDate.toString(),
                contentText = item.text,
                contentImageUrl = item.attachments.firstOrNull()?.photo?.image?.lastOrNull()?.url,
                statistics = listOf(
                    StatisticsItem(StatisticsType.VIEWS, count = item.views.count),
                    StatisticsItem(StatisticsType.SHARE, count = item.reposts.count),
                    StatisticsItem(StatisticsType.COMMENT, count = item.comments.count),
                    StatisticsItem(StatisticsType.FAVORITE, count = item.likes.count),
                )
            )
            mapperResultFeedPost.add(feedPost)
        }
        return mapperResultFeedPost
    }
}