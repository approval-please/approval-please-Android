package com.umc.approval.data.dto.communitydetail.post

import com.google.gson.annotations.SerializedName


data class CommunityTokPostDto (
    @SerializedName("category")
    val category: Int,
    @SerializedName("content")
    val content: String,
    @SerializedName("voteTitle")
    val voteTitle :String?,
    @SerializedName("voteIsSingle")
    val voteIsSingle :Boolean?,
    @SerializedName("voteIsAnonymous")
    val voteIsAnonymous :Boolean?,
    @SerializedName("voteOption")
    val voteOption: List<String>?,
    @SerializedName("link")
    val link: List<String>?,
    @SerializedName("tag")
    val tag: List<String>?,
    @SerializedName("images")
    val images: List<String>?,
  )