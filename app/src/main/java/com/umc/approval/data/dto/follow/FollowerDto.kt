package com.umc.approval.data.dto.follow

import com.google.gson.annotations.SerializedName

data class FollowerDto(
    @SerializedName("profileImg") // 프로필 사진
    val profileImg : String,
    @SerializedName("rank") // 직급
    val rank : Int,
    @SerializedName("nickname") // 이름
    val nickname : String
)