package com.umc.approval.data.dto.mypage

import com.google.gson.annotations.SerializedName
/*
내 정보 구성 data
*/
data class MyPageDto(
    @SerializedName("profileImg") // 프로필 이미지
    val profileImg : String,
    @SerializedName("rank") // 실적
    val rank : Int,
    @SerializedName("nickname") // 이름
    val nickname : String,
    @SerializedName("followerNum") // 팔로워 숫자
    val followerNum : Int,
    @SerializedName("followingNum") // 팔로잉 숫자
    val followingNum : Int,
    @SerializedName("message") // 상태 메세지
    val message : String,
    @SerializedName("curPromotionPoint") // 현재 실적 포인트
    val curPromotionPoint : Int,
    @SerializedName("rankPromotionPoint") // 현재 직급의 최대 실적 포인트
    val rankPromotionPoint : Int,
    @SerializedName("shareLink") // 프로필 공유 링크
    val shareLink : String,
)