package com.umc.approval.data.dto.login.post

import com.google.gson.annotations.SerializedName

data class SocialJoinDto (
        @SerializedName("nickname")
        val nickname : String,
        @SerializedName("phoneNumber")
        var phoneNumber : String,
        @SerializedName("socialType")
        var socialType : String,
)