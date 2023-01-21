package com.umc.approval.data.dto

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
// 팔로잉 탭 구성 data
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
// 팔로워 탭 구성 data
data class FollowerDto(
    @SerializedName("profileImg") // 프로필 사진
    val profileImg : String,
    @SerializedName("rank") // 직급
    val rank : Int,
    @SerializedName("nickname") // 이름
    val nickname : String
)