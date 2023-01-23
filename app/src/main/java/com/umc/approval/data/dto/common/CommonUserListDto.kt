package com.umc.approval.data.dto.common

import com.google.gson.annotations.SerializedName

/**
 * 결재 참여(승인/반려) 유저, 좋아요 누른 유저 정보 담을 DTO
 */
data class CommonUserListDto(
    @SerializedName("data")
    val dataEntity: List<CommonUserDto>
)