package com.umc.approval.data.dto.login.get

import com.google.gson.annotations.SerializedName

/**
 * 일반 로그인 후 반환받는 Dto
 * API 명세서 Check 완료
 * */
data class ReturnBasicLoginDto (
        @SerializedName("accessToken")
        val accessToken : String,
        @SerializedName("refreshToken")
        var refreshToken : String,
)