package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.follow.FollowerDto
import com.umc.approval.data.dto.follow.FollowingDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

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
        @Header("Authorization") accessToken : String) : Call<List<FollowerDto>>

    /**
     * @Post
     * accessToken: 사용자 검증 토큰
     * @Get
     * FollowingDto
     */
    @GET("/profile/my/following")
    @Headers("content-type: application/json")
    fun get_following(
        @Header("Authorization") accessToken : String) : Call<List<FollowingDto>>
}