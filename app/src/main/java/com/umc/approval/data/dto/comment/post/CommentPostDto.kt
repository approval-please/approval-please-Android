package com.umc.approval.data.dto.comment.post

import com.google.gson.annotations.SerializedName

/**
 * 개별 댓글 DTO
 * API 명세서 Check 완료
 * */
data class CommentPostDto(

    @SerializedName("documentId")
    val documentId: Int?=null,
    @SerializedName("reportId")
    val reportId: Int?=null,
    @SerializedName("toktokId")
    val toktokId: Int?=null,
    @SerializedName("parentCommentId")
    val parentCommentId: Int?=null,
    @SerializedName("content")
    val content: String,
    @SerializedName("image")
    val image: String,
)