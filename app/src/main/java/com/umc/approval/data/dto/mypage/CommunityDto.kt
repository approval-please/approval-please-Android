package com.umc.approval.data.dto.mypage

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.community.get.CommunityReport
import com.umc.approval.data.dto.community.get.CommunityTok

data class CommunityDto(
    @SerializedName("toktokCount")
    var toktokCount : Int ?= null,
    @SerializedName("tok-content")
    var communityTok : List<CommunityTok> ?= null,
    @SerializedName("reportCount")
    var reportCount : Int ?= null,
    @SerializedName("report-content")
    var communityReport : List<CommunityReport> ?= null
)
