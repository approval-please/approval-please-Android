package com.umc.approval.data.dto.mypage

import com.google.gson.annotations.SerializedName

data class FollowListDto(
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("totalCount")
    val totalCount : Int,
    @SerializedName("followerCount")
    val followerCount : Int,
    @SerializedName("followingCount")
    val followingCount : Int,
    @SerializedName("content")
    val followDto: List<FollowDto>
)