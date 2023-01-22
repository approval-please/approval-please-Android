package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.login.BasicJoinDto
import com.umc.approval.data.dto.login.SocialJoinDto
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
     * */
    @POST("/auth/email")
    @Headers("content-type: application/json")
    fun email_check(@Query("email") email : String):Call<ResponseBody>

    /**
     * @Post
     * phoneNumber
     * @Get
     * Success: status(0: 번호 존재 X, 1: 번호 존재 O)
     * */
    //phoneNum만 체크하면 될것 같음, 하이픈 제외하고 보내겠음
    @POST("/auth/cert")
    @Headers("content-type: application/json")
    fun phone_check(@Query("phoneNumber") phoneNumber : String):Call<ResponseBody>

    /**
     * @Post
     * BasicJoinDto : nickname, email, password, phoneNumber
     * @Get
     * */
    @POST("/auth/signup")
    @Headers("content-type: application/json")
    fun basic_join(@Query("user") user : BasicJoinDto):Call<ResponseBody>

    /**
     * @Post
     * Header Authorization: Bearer accessToken
     * SocialJoinDto : nickname, phoneNumber, socialType
     * @Get
     * Header Authorization: Bearer Access Token
     * */
    //join하면 다시 로그인 페이지로 이동 아니면 로그인 상태 유지?? social은 바로 토큰 받자!
    //토큰 발급 받자마자 소셜 로그아웃 예정, 토큰 발급 받으면 굳이 다시 로그인할 필요 없음, 다시 로그인 버튼을 누를 필요는 없으므로 이렇게 진행
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