package com.umc.approval.data.dto.search.get

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("level")
    val level: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("userId")
    val userId: Int
)