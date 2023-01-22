package com.umc.approval.data.dto.search.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto


data class Report(
    @SerializedName("commentCount")
    val commentCount: Int,
    @SerializedName("content")
    val content: String,
    @SerializedName("document")
    val document: Document,
    @SerializedName("likeCount")
    val likeCount: Int,
    @SerializedName("linkUrl")
    val linkUrl: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("reportId")
    val reportId: Int,
    @SerializedName("scrapCount")
    val scrapCount: Int,
    @SerializedName("thumbnailImage")
    val thumbnailImage: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("userLevel")
    val userLevel: Int,
    @SerializedName("view")
    val view: Int,
    @SerializedName("openGraph")
    val openGraph: OpenGraphDto
)