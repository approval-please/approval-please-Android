package com.umc.approval.data.dto

import com.google.gson.annotations.SerializedName

data class arankimDto (
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

data class PostVoteDto(
    @SerializedName("content")
    val content: String,
    @SerializedName("participation")
    val participation: ArrayList<PostUserDto>,
)

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

data class PostUserDto (
    @SerializedName("nickName")
    val nickname : String,
    @SerializedName("level")
    val level : String,
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("isFollow")
    val isFollow : Boolean,
    @SerializedName("mine")
    val mine : Boolean
)