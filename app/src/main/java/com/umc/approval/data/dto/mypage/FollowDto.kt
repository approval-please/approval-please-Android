package com.umc.approval.data.dto.mypage

import com.google.gson.annotations.SerializedName

data class FollowDto(
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("level")
    val level : Int,
    @SerializedName("userId")
    val userId : Int,
    @SerializedName("profileImage")
    val profileImage : String,
    @SerializedName("isFollow")
    val isFollow : Boolean
)