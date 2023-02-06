package com.umc.approval.data.dto.mypage

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.community.get.CommunityTokDto

data class MyCommentDto(
    @SerializedName("documentCount")
    val documentCount : Int? = null,
    @SerializedName("approval_content")
    val approval_content : ApprovalPaperDto? = null,
    @SerializedName("toktokCount")
    val toktokCount : Int? = null,
    @SerializedName("toktok_content")
    val tok_content : CommunityTokDto? = null,
    @SerializedName("reportCount")
    val reportCount : Int? = null,
    @SerializedName("report_content")
    val report_content : CommunityReportDto? = null
)
