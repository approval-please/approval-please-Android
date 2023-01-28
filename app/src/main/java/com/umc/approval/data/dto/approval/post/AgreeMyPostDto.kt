package com.umc.approval.data.dto.approval.post

import com.google.gson.annotations.SerializedName

/**
 * 본인 서류 결재 완료 API
 * API 명세서 Check 완료
 * */
data class AgreeMyPostDto(

    @SerializedName("documentId")
    val documentId: Int,
    @SerializedName("isApprove")
    val isApprove: Boolean,

)