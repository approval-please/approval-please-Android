package com.umc.approval.data.dto.login.get

import com.google.gson.annotations.SerializedName

/**
 * 휴대폰 체크 DTO
 * API 명세서 Check 완료
 * */
data class ReturnPhoneAuthRequestDto (
        @SerializedName("requestId")
        var requestId : String,
        @SerializedName("requestTime")
        var requestTime : String,
        @SerializedName("statusCode")
        var statusCode : String,
        @SerializedName("statusName")
        var statusName : String
)