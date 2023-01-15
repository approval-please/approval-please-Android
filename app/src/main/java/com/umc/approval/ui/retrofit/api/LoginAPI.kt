package com.umc.approval.ui.retrofit.api

import com.umc.approval.ui.dto.UserDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * login server와 통신하는 API
 * */
interface LoginAPI {

    /**
     * login API
     * 임시코드, ID, Access 토큰중 한개 전송해서 토큰 발급 받음
     * */
    @POST("/login")
    @Headers("content-type: application/json")
    fun login(@Header("Authorization") idToken:
              String, @Header("LoginCase") case: String):Call<ResponseBody>

    /**
     * User 정보를 가지고 오는
     * */
    @GET("/api/user")
    @Headers("content-type: application/json")
    fun connectServer(@Header("Authorization") accessToken: String):Call<ResponseBody>

    @GET("/userInfo")
    @Headers("content-type: application/json")
    fun userInfo(@Header("Authorization") accessToken: String):Call<UserDto>
}