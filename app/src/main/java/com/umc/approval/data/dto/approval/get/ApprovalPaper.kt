package com.umc.approval.data.dto.approval.get

import com.google.gson.annotations.SerializedName

data class ApprovalPaper(
    @SerializedName("state")
    val state: Int,
    @SerializedName("category")
    val category: Int,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("image")
    val image: List<String>,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("tag")
    val tag: List<String>,
    @SerializedName("view")
    val view: Int,
    @SerializedName("approveCount")
    val approveCount: Int,
    @SerializedName("rejectCount")
    val rejectCount: Int,
)