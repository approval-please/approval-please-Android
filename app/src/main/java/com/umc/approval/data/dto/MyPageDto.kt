package com.umc.approval.data.dto

import com.google.gson.annotations.SerializedName
import java.util.Date
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
    /*
    결재 서류, 커뮤니티, 댓글, 스크랩은 다른 페이지 Fragment 가져오는 거라
    해당 API / DTO / Repository 생길 시 코드 추가
    */
    @SerializedName("recordList") // 실적 탭 data list
    val recordList : List<RecordDTO>
)
/*
실적 탭 구성 data
*/
data class RecordDTO(
    @SerializedName("date") // 날짜
    val date : Date,
    @SerializedName("content") // 내용
    val content : String,
    @SerializedName("point") // 포인트
    val point : Int
)
