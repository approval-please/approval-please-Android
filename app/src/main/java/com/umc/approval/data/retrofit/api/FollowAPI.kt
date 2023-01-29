package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.common.CommonUserListDto
import com.umc.approval.data.dto.follow.FollowStateDto
import retrofit2.Call
import retrofit2.http.*

interface FollowAPI {


    /**
     * @Post
     * accessToken: 사용자 검증 토큰
     * @Get
     * FollowerDto
     */
    @GET("/profile/my/followers")
    @Headers("content-type: application/json")
    fun get_follower(
        @Header("Authorization") accessToken : String, @Query("query") query: String) : Call<CommonUserListDto>

    /**
     * @Post
     * accessToken: 사용자 검증 토큰
     * @Get
     * FollowingDto
     */
    @GET("/profile/my/following")
    @Headers("content-type: application/json")
    fun get_following(
        @Header("Authorization") accessToken : String, @Query("query") query: String) : Call<CommonUserListDto>

    /**
     * @Post
     * accessToken: 사용자 검증 토큰
     * userId: 유저 ID
     * @Get
     * followState: 팔로우 상태
     * 유저 팔로우/언팔로우 API
     */
    @POST("/follow")
    @Headers("content-type: application/json")
    fun follow(
        @Header("Authorization") accessToken: String,
        @Query("userId") userId: Int
    ): Call<FollowStateDto>
}