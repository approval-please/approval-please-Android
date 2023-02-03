package com.umc.approval.data.dto.approval.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**
 * 결재 서류 상세 DTO
 * API 명세서 Check 완료
 * */
data class DocumentDto(

    @SerializedName("documentId")
    val documentId: Int ?= null,
    @SerializedName("reportId")
    val reportId: Int ?= null,
    @SerializedName("state")
    val state: Int ?= null,
    @SerializedName("category")
    val category: Int ?= null,
    @SerializedName("datetime")
    val datetime: String ?= null,
    @SerializedName("profileImage")
    val profileImage: String ?= null,
    @SerializedName("nickname")
    val nickname: String ?= null,
    @SerializedName("imageUrl")
    val imageUrl: List<String> ?= null,
    @SerializedName("title")
    val title: String ?= null,
    @SerializedName("content")
    val content: String ?= null,
    @SerializedName("link")
    val link: OpenGraphDto ?= null,
    @SerializedName("tag")
    val tag: List<String> ?= null,
    @SerializedName("view")
    val view: Int ?= null,
    @SerializedName("level")
    val level: Int ?= null,
    @SerializedName("approveCount")
    var approveCount: Int ?= null,
    @SerializedName("rejectCount")
    var rejectCount: Int ?= null,
    @SerializedName("likedCount")
    val likedCount: Int ?= null,
    @SerializedName("commentCount")
    val commentCount: Int ?= null,
    @SerializedName("isModified")
    val isModified: Boolean ?= null,
    @SerializedName("isWriter")
    val isWriter: Boolean ?= null,
    @SerializedName("reportMade")
    val reportMade: Boolean ?= null,
    @SerializedName("isLiked")
    val isLiked: Boolean ?= null,
    @SerializedName("isScrap")
    val isScrap: Boolean ?= null,
    @SerializedName("isVoted")
    var isVoted: Int ?= null,
    @SerializedName("userId")
    val userId: Int ?= null,
)