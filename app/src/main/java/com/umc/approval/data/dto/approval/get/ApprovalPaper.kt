package com.umc.approval.data.dto.approval.get

import com.google.gson.annotations.SerializedName

/**
 * 결재 서류 DTO
 * API 명세서 Check 완료
 * */
data class ApprovalPaper(
    @SerializedName("state")
    val state: Int,
    @SerializedName("category")
    val category: Int,
    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("images")
    val images: List<String> ?= null,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("tags")
    val tag: List<String> ?= null,
    @SerializedName("view")
    val view: Int,
    @SerializedName("approveCount")
    val approveCount: Int,
    @SerializedName("rejectCount")
    val rejectCount: Int,
)