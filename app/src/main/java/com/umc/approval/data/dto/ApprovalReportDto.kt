package com.umc.approval.data.dto

import com.google.gson.annotations.SerializedName

/**
 * 결재 보고서
 * */
data class ApprovalReportDto(
    @SerializedName("data")
    val dataEntity: List<ApprovalReport>
)

data class ApprovalReport (
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImage")
    val profileImage: String,
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
    val updatedAt: String
)
