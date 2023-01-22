package com.umc.approval.data.dto.search.get


import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

data class Document(
    @SerializedName("approvalCount")
    val approvalCount: Int,
    @SerializedName("category")
    val category: Int,
    @SerializedName("content")
    val content: String,
    @SerializedName("documentId")
    val documentId: Int,
    @SerializedName("imageCount")
    val imageCount: Int,
    @SerializedName("rejectCount")
    val rejectCount: Int,
    @SerializedName("state")
    val state: Int,
    @SerializedName("tag")
    val tag: List<String>,
    @SerializedName("thumbnailImage")
    val thumbnailImage: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("view")
    val view: Int,
    @SerializedName("openGraph")
    val openGraph: OpenGraphDto
)