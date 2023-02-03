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
     * 이메일 체크 API
     * API 명세서 Check 완료
     * 반환값 설정 완료
     * */
    @POST("/auth/email")
    @Headers("content-type: application/json")
    fun email_check(@Body email : String):Call<ReturnEmailCheckDto>

    /**
     * 휴대폰 번호 전송 API
     * API 명세서 Check 완료
     * 반환값 설정 완료
     * */
    @POST("/auth/cert")
    @Headers("content-type: application/json")
    fun phone_auth_request(@Body phoneNumber : String):Call<ReturnPhoneAuthRequestDto>

    /**
     * 인증번호 체크 API
     * API 명세서 Check 완료
     * 반환값 설정 완료
     * */
    @POST("/auth/cert/check")
    @Headers("content-type: application/json")
    fun phone_auth(@Body auth : PhoneAuthDto):Call<ReturnPhoneAuthDto>

    /**
     * 일반 회원가입 API
     * API 명세서 Check 완료
     * 반환값 설정 필요
     * */
    @POST("/auth/signup")
    @Headers("content-type: application/json")
    fun basic_join(@Body basicJoinDto: BasicJoinDto):Call<ReturnBasicLoginDto>

    /**
     * 일반 로그인 API
     * API 명세서 Check 완료
     * 반환값 설정 완료
     * */
    @POST("/auth/login")
    @Headers("content-type: application/json")
    fun basic_login(@Query("email") email : String, @Query("password") password : String):Call<ReturnBasicLoginDto>

    /**
     * SNS 회원가입 API
     * API 명세서 Check 완료
     * 반환값 설정 완료
     * */
    @POST("/auth/signup/sns")
    @Headers("content-type: application/json")
    fun social_join(@Body user : SocialJoinDto):Call<ReturnBasicLoginDto>

    /**
     * 카카오 로그인 API
     * API 명세서 Check 완료
     * 반환값 설정 완료
     * */
    @POST("/auth/kakao")
    @Headers("content-type: application/json")
    fun social_login(@Header("Authorization") accessToken : String, @Body email: String):Call<ReturnSocialLoginDto>

    /**
     * 비밀번호 변경 API
     * API 명세서 Check 완료
     * 반환값 설정 완료
     * */
    @POST("/auth/reset")
    @Headers("content-type: application/json")
    fun password_change(@Body passwordChangeDto: PasswordChangeDto):Call<ResponseBody>

    /**
     * 로그아웃 API
     * API 명세서 Check 완료
     * 반환값 설정 완료
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