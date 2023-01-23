package com.umc.approval.data.dto.common

import com.google.gson.annotations.SerializedName

/**
 * Follow + Participants + Likes Common
 * */
data class CommonUserDto(
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("level")
    val level: Int,
    @SerializedName("isFollow")
    val isFollow: Boolean
)