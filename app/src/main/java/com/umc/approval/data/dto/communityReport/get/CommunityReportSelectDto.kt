package com.umc.approval.data.dto.communityReport.get

import com.google.gson.annotations.SerializedName

/* 결재 보고서
게시글 등록 시 결재 서류 선택 */
data class CommunityReportSelectDto(
    @SerializedName("content")
    var content: List<CommunityReportSelect>
)
