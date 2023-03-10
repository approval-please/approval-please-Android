package com.umc.approval.data.dto.login.post

import com.google.gson.annotations.SerializedName

/**
 * 일반 로그인시 Dto
 * API 명세서 Check 완료
 * */
data class BasicLoginDto (
        @SerializedName("email")
        var email : String,
        @SerializedName("password")
        var password : String,
)