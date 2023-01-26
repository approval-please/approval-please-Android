package com.umc.approval.data.dto.comment

import com.google.gson.annotations.SerializedName

/**
 * 개별 댓글 DTO
 * API 명세서 Check 완료
 * */
data class DocumentCommentDto(
    @SerializedName("documentId")
    val documentId : Int
)