package com.umc.approval.data.dto.common

import com.google.gson.annotations.SerializedName

/**
 * 좋아요 누른 유저 정보 담을 DTO
 */
data class CommonUserListDto(
    @SerializedName("likeCount")
    val likeCount: Int,
    @SerializedName("content")
    val dataEntity: List<CommonUserDto>
)