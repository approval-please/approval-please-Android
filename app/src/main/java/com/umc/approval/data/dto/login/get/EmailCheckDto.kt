package com.umc.approval.data.dto.login.get

import com.google.gson.annotations.SerializedName

/**
 * 이메일 체크 후 반환받는 Dto
 * API 명세서 Check 완료
 * */
data class EmailCheckDto (
        @SerializedName("status")
        val status : Int,
        @SerializedName("email")
        var email : String,
        @SerializedName("ㅊ")
        var socialType : String
)