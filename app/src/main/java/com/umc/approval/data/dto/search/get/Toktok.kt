package com.umc.approval.data.dto.search.get


import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

data class Toktok(
    @SerializedName("category")
    val category: Int,
    @SerializedName("content")
    val content: String,
    @SerializedName("linkUrl")
    val linkUrl: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("toktokId")
    val toktokId: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("userLevel")
    val userLevel: Int,
    @SerializedName("vote")
    val vote: Vote,
    @SerializedName("openGraph")
    val openGraph: OpenGraphDto
)