package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.login.get.*
import com.umc.approval.data.dto.login.post.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * 로그인 및 회원가입 관련 기능 API
 * */
interface LoginAPI {

    /**
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
    fun basic_join(@Body basicJoinDto: BasicJoinDto):Call<ReturnBasicLoginDto>

    /**
     * @Post
     * BasicLoginDto
     * @Get
     * Access Token
     * API 명세서 Check 완료
     * */
    @POST("/auth/login")
    @Headers("content-type: application/json")
    fun basic_login(@Query("email") email : String, @Query("password") password : String):Call<ReturnBasicLoginDto>

    /**
     * @Post
     * Header Authorization: Bearer accessToken
     * SocialJoinDto : nickname, phoneNumber, socialType
     * @Get
     * Header Authorization: Bearer Access Token
     * */
    @POST("/auth/signup/sns")
    @Headers("content-type: application/json")
    fun social_join(@Body user : SocialJoinDto):Call<ReturnBasicLoginDto>

    /**
     * @Post
     * Header Authorization: Bearer accessToken
     * @Get
     * Header Authorization : Bearer + 토크 값
     * */
    @POST("/auth/kakao")
    @Headers("content-type: application/json")
    fun social_login(@Header("Authorization") accessToken : String):Call<ReturnSocialLoginDto>

    /**
     * @Post
     * email, password
     * @Get
     * */
    @POST("/auth/reset")
    @Headers("content-type: application/json")
    fun password_change(@Body passwordChangeDto: PasswordChangeDto):Call<ResponseBody>

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