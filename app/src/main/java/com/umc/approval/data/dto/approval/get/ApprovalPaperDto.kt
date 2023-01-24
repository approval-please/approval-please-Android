package com.umc.approval.data.dto.approval.get

import com.google.gson.annotations.SerializedName

/**
 * 결재 서류
 * */
data class ApprovalPaperDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("totalPage")
    val totalPage: Int,
    @SerializedName("totalElement")
    val totalElement: Int,
    @SerializedName("content")
    val approvalPaperDto: List<ApprovalPaper>
)
