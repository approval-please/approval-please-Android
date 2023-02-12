package com.umc.approval.data.dto.common

import com.google.gson.annotations.SerializedName

/**
 * Likes Common
 * */
data class CommonUserDto(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("level")
    val level: Int,
    @SerializedName("isFollow")
    val isFollow: Boolean,
    @SerializedName("isMy")
    val isMy: Boolean
)