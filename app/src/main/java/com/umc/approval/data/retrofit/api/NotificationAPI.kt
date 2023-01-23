package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.NotificationDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

/*
Notification API
 */
interface NotificationAPI {
    @GET("/notification")
    @Headers("content-type: application/json")
    // NotificationFragment의 '전체' 탭 알림 List 정보 가지고 옴
    fun getNotification(
        @Header("Authorization") accessToken : String) : Call<NotificationDTO>
    // NotificationFragment의 '활동' 탭 알림 List 정보 가지고 옴
    fun getNotificationActivity(
        @Header("Authorization") accessToken: String) : Call<NotificationDTO>
    // NotificationFragment의 '서류' 탭 알림 List 정보 가지고 옴
    fun getNotificationDocument(
        @Header("Authorization") accessToken: String) : Call<NotificationDTO>
}