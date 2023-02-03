package com.umc.approval.data.dto.comment.post

import com.google.gson.annotations.SerializedName

/**
 * 개별 댓글 DTO
 * API 명세서 Check 완료
 * */
data class CommentPostDto(

    @SerializedName("documentId")
    var documentId: Int?=null,
    @SerializedName("reportId")
    var reportId: Int?=null,
    @SerializedName("toktokId")
    var toktokId: Int?=null,
    @SerializedName("parentCommentId")
    var parentCommentId: Int?=null,
    @SerializedName("content")
    var content: String ?= null,
    @SerializedName("image")
    var image: String ?= null,
)