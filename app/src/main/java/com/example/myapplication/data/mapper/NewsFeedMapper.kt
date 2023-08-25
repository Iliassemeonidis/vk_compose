package com.example.myapplication.data.mapper

import android.icu.text.SimpleDateFormat
import com.example.myapplication.data.model.NewsFeedResponseDto
import com.example.myapplication.data.model.ResponseCommentDto
import com.example.myapplication.domain.FeedPost
import com.example.myapplication.domain.PostComment
import com.example.myapplication.domain.StatisticsItem
import com.example.myapplication.domain.StatisticsType
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

object NewsFeedMapper {

    fun mapResponseToPosts(newsFeedResponseDto: NewsFeedResponseDto): List<FeedPost> {
        val mapperResultFeedPost = mutableListOf<FeedPost>()

        val items = newsFeedResponseDto.newsFeed.items
        val groups = newsFeedResponseDto.newsFeed.groups

        items.forEach { item ->
            val userGroup =
                groups.find { it.groupId.absoluteValue == item.id.absoluteValue } ?: return@forEach

            //fixme разобраться в краше
            if (item.attachments == null) return@forEach

            val feedPost = FeedPost(
                id = item.id.toLong(),
                postId = item.post_id,
                groupId = userGroup.groupId,
                profileUrl = userGroup.groupPhoto,
                groupName = userGroup.groupName,
                publishDate = mapDateFormat(item.publishDate * 1000),
                contentText = item.text,
                contentImageUrl = item.attachments.firstOrNull()?.photo?.image?.lastOrNull()?.url,
                isLiked = item.likes.userLikes > 0,
                statistics = listOf(
                    StatisticsItem(StatisticsType.VIEWS, count = item.views.count ?: 0),
                    StatisticsItem(StatisticsType.SHARE, count = item.reposts.count),
                    StatisticsItem(StatisticsType.COMMENT, count = item.comments.count),
                    StatisticsItem(StatisticsType.FAVORITE, count = item.likes.count),
                )
            )
            mapperResultFeedPost.add(feedPost)
        }
        return mapperResultFeedPost
    }

    private fun mapDateFormat(tinsity: Long): String {
        val date = Date(tinsity)
        return SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(date)
    }

    fun mapResponseToComment(responseComment: ResponseCommentDto): List<PostComment> {
        val mapperCommentResult = mutableListOf<PostComment>()

        val items = responseComment.comment.items
        val profile = responseComment.comment.profiles

        if (items.isEmpty()) return emptyList()
        items.forEach { item ->
            val response = profile.find { it.userId == item.authorCommentId } ?: return@forEach
            if (item.commentText.isEmpty()) return@forEach
            val result = PostComment(
                id = item.commentId,
                iconId = response.userPhoto,
                authorName = "${response.firstName} ${response.lastName}",
                comment = item.commentText,
                publishDate = mapDateFormat(item.publishDate)
                )
            mapperCommentResult.add(result)
        }
        return mapperCommentResult
    }

}

