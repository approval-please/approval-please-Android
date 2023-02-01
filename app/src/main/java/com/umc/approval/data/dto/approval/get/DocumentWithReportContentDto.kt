package com.umc.approval.data.dto.approval.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**
 * 리포트와 연동하는 결재 서류 DTO
 * API 명세서 Check 완료
 * */
data class DocumentWithReportContentDto(

    @SerializedName("content")
    val content: List<DocumentWithReportDto> ?= null,
)