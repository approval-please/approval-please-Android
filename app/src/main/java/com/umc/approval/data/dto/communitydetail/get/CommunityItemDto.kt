package com.umc.approval.data.dto.communitydetail.get

import com.google.gson.annotations.SerializedName

data class CommunityItemDto (
    @SerializedName("writer")
    val user: PostUserDto,
    @SerializedName("content")
    val content: String,
    @SerializedName("imageUrl")
    val imageUrl: List<String>,
    @SerializedName("view")
    val view: Int,
    @SerializedName("likeCount")
    val likeCount: Int,
    @SerializedName("commentCount")
    val commentCount: Int,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("department")
    val department : String,
    @SerializedName("voteTitle")
    val voteTitle :String,
    @SerializedName("voteContent")
    val voteItem : List<PostVoteDto>,
    @SerializedName("link")
    val link :String,
    @SerializedName("comment")
    val comment : List<PostCommentDto>,
    )