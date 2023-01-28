package com.umc.approval.data.dto.comment.get

import com.google.gson.annotations.SerializedName

/**
 * 개별 댓글 DTO
 * API 명세서 Check 완료
 * */
data class CommentDto(
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("level")
    val level: Int,
    @SerializedName("commentId")
    val commentId: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("isWriter")
    val isWriter: Boolean,
    @SerializedName("isMy")
    val isMy: Boolean,
    @SerializedName("isLike")
    val isLike: Boolean,
    @SerializedName("isDeleted")
    val isDeleted: Boolean,
    @SerializedName("likeCount")
    val likeCount: Int,
    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("childComment")
    val childComment: List<CommentDto>
)