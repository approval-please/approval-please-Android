package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.approval.post.LikeDto
import com.umc.approval.data.dto.follow.FollowStateDto
import com.umc.approval.data.dto.follow.NotificationStateDto
import com.umc.approval.data.dto.follow.ScrapStateDto
import com.umc.approval.data.dto.mypage.FollowListDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface FollowAPI {


    /**
     * 내 팔로잉 목록 조회
     * */
    @GET("/profile/my/followings")
    @Headers("content-type: application/json")
    fun get_my_followings(
        @Header("Authorization") accessToken : String) : Call<FollowListDto>

    /**
     * 내 팔로우 목록 조회 API
     * */
    @GET("/profile/my/followers")
    @Headers("content-type: application/json")
    fun get_my_followers(
        @Header("Authorization") accessToken : String) : Call<FollowListDto>

    /**
     * 유저 팔로우/언팔로우 API
     */
    @POST("/follow")
    @Headers("content-type: application/json")
    fun follow(
        @Header("Authorization") accessToken: String,
        @Body toUserId: Int
    ): Call<FollowStateDto>

    /**
     * 스크랩 API
     */
    @POST("/scrap")
    @Headers("content-type: application/json")
    fun scrap(
        @Header("Authorization") accessToken: String,
        @Body likeDto: LikeDto
    ): Call<ScrapStateDto>

    /**
     * 알림설정 API
     */
    @POST("/notification")
    @Headers("content-type: application/json")
    fun notification(
        @Header("Authorization") accessToken: String,
        @Body likeDto: LikeDto
    ): Call<NotificationStateDto>

    /**
     * 신고 API
     */
    @POST("/accuse")
    @Headers("content-type: application/json")
    fun accuse(
        @Header("Authorization") accessToken: String,
        @Body likeDto: LikeDto
    ): Call<ResponseBody>
}