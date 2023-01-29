package com.umc.approval.data.dto.login.post

import com.google.gson.annotations.SerializedName

/**
 * 휴대폰 인증번호 DTO
 * API 명세서 Check 완료
 * */
data class PhoneAuthDto (
        @SerializedName("phoneNumber")
        val phoneNumber : String,
        @SerializedName("certNumber")
        var certNumber : String,
)