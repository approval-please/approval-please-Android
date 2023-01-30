package com.umc.approval.data.dto.approval.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**
 * 목록에서 결재 서류 DTO
 * API 명세서 Check 완료
 * */
data class ApprovalPaper(
    @SerializedName("documentId")
    val documentId: Int,
    @SerializedName("category")
    val category: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("tag")
    val tag: List<String>,
    @SerializedName("link")
    val link: OpenGraphDto? = null,
    @SerializedName("thumbnailImage")
    val thumbnailImage: String,
    @SerializedName("imageCount")
    val imageCount: Int,
    @SerializedName("state")
    val state: Int,
    @SerializedName("approveCount")
    val approveCount: Int,
    @SerializedName("rejectCount")
    val rejectCount: Int,
    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("view")
    val view: Int,
)