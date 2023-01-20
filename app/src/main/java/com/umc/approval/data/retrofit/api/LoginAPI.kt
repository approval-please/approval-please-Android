package com.umc.approval.data.retrofit.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * login API
 * */
interface LoginAPI {

    /**
     * @Post
     * Header Authorization : idToken(kakao accessToken or google idToken)
     * Header LoginCase : kakao, google, naver
     * @Get
     * Header Authorization : Bearer + 토크 값
     * social login api
     * */
    @POST("/login")
    @Headers("content-type: application/json")
    fun social_login(@Header("Authorization") idToken:
              String, @Header("LoginCase") case: String):Call<ResponseBody>

    /**
     * @Post
     * email, password
     * @Get
     * Header Authorization : Bearer + 토크 값
     * basic login api
     * */
    @POST("/login")
    @Headers("content-type: application/json")
    fun basic_login(@Query("email") email : String, @Query("password") password : String):Call<ResponseBody>

    /**
     * @Post
     * accessToken : 사용자 검증 토큰
     * @Get
     * Header Authorization : Bearer + 토크 값
     * social login api
     * */
    @GET("/api/user")
    @Headers("content-type: application/json")
    fun connect_server(@Header("Authorization") accessToken: String):Call<ResponseBody>
}