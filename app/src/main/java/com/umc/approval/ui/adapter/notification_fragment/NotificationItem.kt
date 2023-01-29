package com.umc.approval.ui.adapter.notification_fragment
/* 알림 recyclerView Item을 위한 data class */

data class NotificationItem(val type : Int, val title1 : String, val title2 : String, val content : String, val date : String){
    companion object{
        const val TYPE_1 = 0; // 서류 제출 알림
        const val TYPE_2 = 1; // 댓글 알림
        const val TYPE_3 = 2; // 승인 알림
        const val TYPE_4 = 3; // 반려 알림
    }
}