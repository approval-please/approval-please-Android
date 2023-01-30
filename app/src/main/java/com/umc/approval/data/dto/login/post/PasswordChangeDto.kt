package com.umc.approval.data.dto.login.post

import com.google.gson.annotations.SerializedName

/**
 * 일반 회원가입시 Dto
 * API 명세서 Check 완료
 * */
data class PasswordChangeDto (
        @SerializedName("email")
        var email : String,
        @SerializedName("newPassword")
        var newPassword : String,
)