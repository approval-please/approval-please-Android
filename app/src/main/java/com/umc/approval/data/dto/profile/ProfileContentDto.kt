package com.umc.approval.data.dto.profile

import com.google.gson.annotations.SerializedName

/**
 * 프로필 화면 Dto
 * API 명세서 Check 완료
 * */
data class ProfileContentDto (
        @SerializedName("isFollow")
        var isFollow : Boolean,
        @SerializedName("content")
        var content : ProfileDto,
)