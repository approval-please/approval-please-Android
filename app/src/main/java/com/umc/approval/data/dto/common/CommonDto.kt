package com.umc.approval.data.dto.common

import com.google.gson.annotations.SerializedName

/**
 * 스크랩 및
 * API 명세서 Check 완료
 * */
data class CommonDto(

    @SerializedName("documentId")
    var documentId: Int?=null,
    @SerializedName("reportId")
    var reportId: Int?=null,
    @SerializedName("toktokId")
    var toktokId: Int?=null,
)