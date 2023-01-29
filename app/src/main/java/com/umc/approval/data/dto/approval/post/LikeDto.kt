package com.umc.approval.data.dto.approval.post

import com.google.gson.annotations.SerializedName

/**
 * 동의 및 거절 수 API
 * API 명세서 Check 완료
 * */
data class LikeDto(

    @SerializedName("documentId")
    val documentId: Int ?= null,
    @SerializedName("toktokId")
    val toktokId: Int ?= null,
    @SerializedName("reportId")
    val reportId: Int ?= null,
    @SerializedName("commentId")
    val commentId: Int ?= null,

)