package com.umc.approval.data.dto.mypage

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.community.get.CommunityReport
import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.community.get.CommunityTok
import com.umc.approval.data.dto.community.get.CommunityTokDto

data class MyCommentDto(
    @SerializedName("documentCount")
    val documentCount : Int? = null,
    @SerializedName("documentContent")
    val documentContent : List<ApprovalPaper>? = null,
    @SerializedName("toktokCount")
    val toktokCount : Int? = null,
    @SerializedName("toktokContent")
    val toktokContent : List<CommunityTok>? = null,
    @SerializedName("reportCount")
    val reportCount : Int? = null,
    @SerializedName("reportContent")
    val reportContent : List<CommunityReport>? = null
)
