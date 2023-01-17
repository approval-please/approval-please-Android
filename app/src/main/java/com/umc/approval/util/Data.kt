package com.umc.approval.util

data class ApprovalPaper(
    val approval_status: Int, // 0: 승인됨, 1: 반려됨, 2: 승인대기중
    val title: String,
    val content: String,
    val approve_count: Int,
    val reject_count: Int,
    val views: Int,
    val department: String,
    val date: String,  // 서버 측에서 보내주는 데이터 가공 필요
)

data class Post(
    val user_profile_thumbnail: String,
    val user_nickname: String,
    val user_rank: String,
    val views: Int,
    val content: String,
    val comment_count: Int,
    val like_count: Int,
    val date: String,
)

data class ApprovalReport(
    val user_profile_thumbnail: String,
    val user_nickname: String,
    val user_rank: String,
    val title: String,
    val content: String,
    val image_path: String,
    val views: Int,
    val comment_count: Int,
    val like_count: Int,
    val date: String,
)

data class Participant(
    val user_profile_image: String,
    val user_rank: String,
    val user_nickname: String,
    val follow_status: Boolean,
)