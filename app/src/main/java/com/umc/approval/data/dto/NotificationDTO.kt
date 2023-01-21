package com.umc.approval.data.dto
import com.google.gson.annotations.SerializedName
import java.util.Date

    /*
    알림 Fragment의 전체 / 활동 / 서류 탭에서 보여질 알림 리스트
    */
data class NotificationDTO (
    @SerializedName("notificationList")
    val notificationList: List<NotificationData>)
    /*
    개별 알림 data
    */
data class NotificationData(
    @SerializedName("profileImg")
    val profileImg : String, // 프로필 사진
    @SerializedName("state")
    val state : Int, // 제출, 댓글, 반려, 승인 상태
    @SerializedName("otherUserNickname")
    val otherUserNickname : String, // 댓글, 승인, 반려한 사람의 이름
    @SerializedName("otherUserRank")
    val otherUserRank : Int, // 댓글, 승인, 반려한 사람의 직급
    @SerializedName("content")
    val content : String, // 알림 상세 내용
    @SerializedName("date")
    val date : Date // 날짜
)