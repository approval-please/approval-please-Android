package com.umc.approval.data.dto.mypage

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.community.get.CommunityReport
import com.umc.approval.data.dto.community.get.CommunityTok

data class MyScrapDto(
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
