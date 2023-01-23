package com.umc.approval.data.dto.communitydetail.get

import com.google.gson.annotations.SerializedName

data class PostCommentDto(
    @SerializedName("content")
    val content: String,
    @SerializedName("commenter")
    val commenter: PostUserDto,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("likeCount")
    val likeCount: Int,
)