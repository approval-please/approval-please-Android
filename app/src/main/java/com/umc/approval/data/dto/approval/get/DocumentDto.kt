package com.umc.approval.data.dto.approval.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

data class DocumentDto(
    @SerializedName("state")
    val state: Int,
    @SerializedName("category")
    val category: Int,
    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("imageUrl")
    val imageUrl: List<String>,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("link")
    val link: OpenGraphDto,
    @SerializedName("tag")
    val tag: List<String>,
    @SerializedName("view")
    val view: Int,
    @SerializedName("level")
    val level: Int,
    @SerializedName("approveCount")
    val approveCount: Int,
    @SerializedName("rejectCount")
    val rejectCount: Int,
    @SerializedName("likedCount")
    val likedCount: Int,
    @SerializedName("commentCount")
    val commentCount: Int,
)