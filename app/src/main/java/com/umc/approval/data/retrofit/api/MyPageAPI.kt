package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.MyPageDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface MyPageAPI {
    @GET("/notification")
    @Headers("content-type: application/json")
    // MypageFragment 구성 정보 가지고 옴
    fun getMyPage(
        @Header("Authorization") idToken : String) : Call<MyPageDto>
}