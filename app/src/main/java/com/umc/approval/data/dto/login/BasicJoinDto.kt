package com.umc.approval.data.dto.login

import com.google.gson.annotations.SerializedName

data class BasicJoinDto (
        @SerializedName("nickname")
        val nickname : String,
        @SerializedName("email")
        var email : String,
        @SerializedName("password")
        var password : String,
        @SerializedName("phoneNumber")
        var phoneNumber : String,
)