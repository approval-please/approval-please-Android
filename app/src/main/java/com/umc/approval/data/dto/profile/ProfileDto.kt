package com.umc.approval.data.dto.profile

import com.google.gson.annotations.SerializedName

/**
 * 프로필 화면 Dto
 * API 명세서 Check 완료
 * */
data class ProfileDto (
        @SerializedName("nickname")
        var nickname : String,
        @SerializedName("profileImage")
        var profileImage : String,
        @SerializedName("introduction")
        var introduction : String,
        @SerializedName("promotionPoint")
        var promotionPoint : Int,
        @SerializedName("level")
        var level : Int,
        @SerializedName("followings")
        var followings : Int,
        @SerializedName("follows")
        var follows : Int,
        @SerializedName("isFollow")
        var isFollow : Boolean
)