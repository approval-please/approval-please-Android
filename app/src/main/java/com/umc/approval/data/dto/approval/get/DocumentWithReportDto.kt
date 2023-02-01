package com.umc.approval.data.dto.approval.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**
 * 리포트와 연동하는 결재 서류 DTO
 * API 명세서 Check 완료
 * */
data class DocumentWithReportDto(

    @SerializedName("documentId")
    val documentId: Int ?= null,
    @SerializedName("title")
    val title: String ?= null,
    @SerializedName("state")
    val state: Int ?= null,
    @SerializedName("datetime")
    val datetime: String ?= null,
)