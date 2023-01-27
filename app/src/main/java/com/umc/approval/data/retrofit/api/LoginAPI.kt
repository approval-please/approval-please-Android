package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.login.get.ReturnEmailCheckDto
import com.umc.approval.data.dto.login.get.ReturnPhoneAuthDto
import com.umc.approval.data.dto.login.get.ReturnPhoneAuthRequestDto
import com.umc.approval.data.dto.login.post.BasicJoinDto
import com.umc.approval.data.dto.login.post.PhoneAuthDto
import com.umc.approval.data.dto.login.post.SocialJoinDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * login API
 * */
interface LoginAPI {

    /**
     * @Post
     * email
     * @Get
     * Success: status(0: 계정 존재 X, 1: 일반 계정 존재, 2: SNS 계정 존재)
     * API 명세서 Check 완료
     * 반환값 설정 완료
     * */
    @POST("/auth/email")
    @Headers("content-type: application/json")
    fun email_check(@Body email : String):Call<ReturnEmailCheckDto>

    /**
     * @Post
     * phoneNumber
     * @Get
     * API 명세서 Check 완료
     * 반환값 설정 완료
     * */
    @POST("/auth/cert")
    @Headers("content-type: application/json")
    fun phone_auth_request(@Body phoneNumber : String):Call<ReturnPhoneAuthRequestDto>

    /**
     * @Post
     * phoneNumber
     * @Get
     * API 명세서 Check 완료
     * 반환값 설정 완료
     * */
    @POST("/auth/cert/check")
    @Headers("content-type: application/json")
    fun phone_auth(@Body auth : PhoneAuthDto):Call<ReturnPhoneAuthDto>

    /**
     * @Post
     * BasicJoinDto
     * @Get
     * API 명세서 Check 완료
     * 반환값 설정 필요
     * */
    @POST("/auth/signup")
    @Headers("content-type: application/json")
    fun basic_join(@Body basicJoinDto: BasicJoinDto):Call<ResponseBody>

    /**
     * @Post
     * Header Authorization: Bearer accessToken
     * SocialJoinDto : nickname, phoneNumber, socialType
     * @Get
     * Header Authorization: Bearer Access Token
     * */
    @POST("/auth/signup/sns")
    @Headers("content-type: application/json")
    fun social_join(@Header("Authorization") accessToken: String, @Query("user") user : SocialJoinDto):Call<ResponseBody>

    /**
     * @Post
     * email, password
     * @Get
     * Header Authorization : Bearer + 토크 값
     * */
    @POST("/auth/login")
    @Headers("content-type: application/json")
    fun basic_login(@Query("email") email : String, @Query("password") password : String):Call<ResponseBody>

    /**
     * @Post
     * Header Authorization: Bearer accessToken
     * @Get
     * Header Authorization : Bearer + 토크 값
     * */
    @POST("/auth/kakao")
    @Headers("content-type: application/json")
    fun social_login(@Header("Authorization") accessToken : String):Call<ResponseBody>

    /**
     * @Post
     * email, password
     * @Get
     * */
    @POST("/auth/reset")
    @Headers("content-type: application/json")
    fun password_change(@Query("email") email : String, @Query("password") password : String):Call<ResponseBody>

    /**
     * @Post
     * Header Authorization: Bearer accessToken
     * @Get
     * */
    @POST("/auth/logout")
    @Headers("content-type: application/json")
    fun logout(@Header("Authorization") accessToken : String):Call<ResponseBody>

    /**
     * @Post
     * Header Authorization : Bearer accessToken
     * Header LoginCase : kakao, google, naver
     * @Get
     * Header Authorization : Bearer + 토크 값
     * social login api
     * */
    @POST("/login")
    @Headers("content-type: application/json")
    fun login(@Header("Authorization") accessToken:
                     String, @Header("LoginCase") case: String):Call<ResponseBody>

    /**
     * @Post
     * accessToken : 사용자 검증 토큰
     * @Get
     * Header Authorization : Bearer + 토크 값
     * */
    //토큰 유효성 검사
    @GET("/api/user")
    @Headers("content-type: application/json")
    fun connect_server(@Header("Authorization") accessToken: String):Call<ResponseBody>
}