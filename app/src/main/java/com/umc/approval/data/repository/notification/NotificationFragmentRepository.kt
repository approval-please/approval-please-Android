package com.umc.approval.data.repository.notification

import com.umc.approval.data.dto.NotificationDTO
import com.umc.approval.data.retrofit.instance.RetrofitInstance.notificationApi
import retrofit2.Call

class NotificationFragmentRepository() {
    // '전체' 탭 알림 List 정보 가지고 오는 API 호출
    fun getNotification(idToken : String) : Call<NotificationDTO>{
        return notificationApi.getNotification(idToken)
    }
    // '활동' 탭 알림 List 정보 가지고 오는 API 호출
    fun getNotificationActivity(idToken : String) : Call<NotificationDTO>{
        return notificationApi.getNotificationActivity(idToken)
    }
    // '서류' 탭 알림 List 정보 가지고 오는 API 호출
    fun getNotificationDocument(idToken : String) : Call<NotificationDTO>{
        return notificationApi.getNotificationDocument(idToken)
    }
}