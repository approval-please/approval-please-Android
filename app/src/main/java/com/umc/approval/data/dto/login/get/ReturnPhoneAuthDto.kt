package com.umc.approval.data.dto.login.get

import com.google.gson.annotations.SerializedName

/**
 * Auth 체크 DTO
 * API 명세서 Check 완료
 * */
data class ReturnPhoneAuthDto (
        @SerializedName("isDuplicate")
        var isDuplicate : Boolean,
        @SerializedName("email")
        var email : String,
        @SerializedName("socialType")
        var socialType : String,
)