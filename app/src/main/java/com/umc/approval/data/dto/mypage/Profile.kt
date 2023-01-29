package com.umc.approval.data.dto.mypage

import com.google.gson.annotations.SerializedName

/**mypage profile data*/
data class Profile(
    @SerializedName("profileImage") // 프로필 이미지
    val profileImage : String? = null,
    @SerializedName("level") //직급
    val level : Int,
    @SerializedName("nickname") // 닉네임
    val nickname : String,
    @SerializedName("follows") // 팔로워 숫자
    val follows : Int,
    @SerializedName("followings") // 팔로잉 숫자
    val followings : Int,
    @SerializedName("promotionPoint") // 현재 포인트
    val promotionPoint : Int,
    @SerializedName("introduction") // 현재 포인트
    val introduction : String? = null,
)