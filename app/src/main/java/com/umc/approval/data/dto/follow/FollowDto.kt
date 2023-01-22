package com.umc.approval.data.dto.follow

import com.google.gson.annotations.SerializedName

/*
FollowFragment DTO
*/
data class FollowDto (
    @SerializedName("userNickname")
    val userNickname : String, // 사용자 이름
    @SerializedName("FollowingData")
    val followingList : List<FollowingDto>, // FollowingFragment 탭 구성 아이템 List
    @SerializedName("FollowerData")
    val followerList : List<FollowerDto> // FollowerFragment 탭 구성 아이템 List
)