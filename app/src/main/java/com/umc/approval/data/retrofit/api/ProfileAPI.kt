package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.profile.ProfileDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Profile Get or Change API
 * */
interface ProfileAPI {

    /**
     * @Post
     * accessToken : 사용자 검증 토큰
     * profile : nickname, introduce, image 정보
     * 프로필 변경 API
     * */
    @POST("/profile/update")
    @Headers("content-type: application/json")
    fun profile_change(
        @Header("Authorization") accessToken: String,
        @Query("profile") profile: ProfileDto):Call<ResponseBody>


    /**
     * @Post
     * accessToken : 사용자 검증 토큰
     * @Get
     * profile : nickname, introduce, image 정보
     * 프로필 정보 로드 API
     * */
    @GET("/profile/update")
    @Headers("content-type: application/json")
    fun profile_get(
        @Header("Authorization") accessToken: String):Call<ProfileDto>
}