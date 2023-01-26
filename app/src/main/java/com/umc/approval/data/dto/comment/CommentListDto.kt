package com.umc.approval.data.dto.comment

import com.google.gson.annotations.SerializedName

/**
 * 댓글 목록 DTO
 * API 명세서 Check 완료
 * */
data class CommentListDto(
    @SerializedName("commentCount")
    val commentCount: Int,
    @SerializedName("content")
    val content: List<CommentDto>
)