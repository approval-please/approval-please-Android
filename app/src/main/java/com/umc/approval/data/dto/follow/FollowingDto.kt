package com.umc.approval.data.dto.follow

import com.google.gson.annotations.SerializedName

data class FollowingDto(
    @SerializedName("profileImg") // 프로필 사진
    val profileImg : String,
    @SerializedName("rank") // 직급
    val rank : Int,
    @SerializedName("nickname") // 이름
    val nickname : String,
    @SerializedName("isFollowing") // 대상 사용자가 팔로잉 중인 상태인지
    val isFollowing : Boolean
)