package com.umc.approval.data.dto.approval.get

import com.google.gson.annotations.SerializedName

/**
 * 결재 서류
 * */
data class ApprovalPaperDto(
    @SerializedName("approvalPaperDto")
    val approvalPaperDto: List<ApprovalPaper>
)
