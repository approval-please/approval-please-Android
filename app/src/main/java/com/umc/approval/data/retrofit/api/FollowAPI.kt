package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.FollowDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface FollowAPI {
    @GET("/follow")
    @Headers("content-type: application/json")
    // FollowFragment에 들어갈 구성 데이터를 가지고 옴
    // FollowingFragment + FollowerFragment의 리스트 아이템 data
    fun getFollowData(
        @Header("Authorization") idToken : String) : Call<FollowDto>
}