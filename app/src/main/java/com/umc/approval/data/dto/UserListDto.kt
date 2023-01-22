package com.umc.approval.data.dto

import com.google.gson.annotations.SerializedName

/**
 * 결재 참여(승인/반려) 유저, 좋아요 누른 유저 정보 담을 DTO
 */
data class UserListDto(
    @SerializedName("data")
    val dataEntity: List<UsersDto>
)

data class UsersDto(
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("level")
    val level: Int,
    @SerializedName("isFollow")
    val isFollow: Boolean
)


