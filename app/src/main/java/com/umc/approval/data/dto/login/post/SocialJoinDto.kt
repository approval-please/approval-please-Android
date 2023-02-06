package com.umc.approval.data.dto.login.post

import com.google.gson.annotations.SerializedName

/**
 * 소셜 회원가입시 Dto
 * API 명세서 Check 완료
 * */
data class SocialJoinDto (
        @SerializedName("nickname")
        val nickname : String,
        @SerializedName("email")
        val email : String,
        @SerializedName("socialType")
        val socialType : String,
        @SerializedName("phoneNumber")
        var phoneNumber : String,
        @SerializedName("socialId")
        var socialId : String,
)