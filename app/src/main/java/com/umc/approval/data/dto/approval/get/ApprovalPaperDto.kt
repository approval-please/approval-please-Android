package com.umc.approval.data.dto.approval.get

import com.google.gson.annotations.SerializedName

/**
 * 결재 서류 목록 DTO
 * API 명세서 Check 완료
 * */
data class ApprovalPaperDto(
    @SerializedName("content")
    val approvalPaperDto: List<ApprovalPaper>
)
