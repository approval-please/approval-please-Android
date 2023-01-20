package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.FollowStateDto
import com.umc.approval.data.dto.UserListDto
import retrofit2.Call
import retrofit2.http.*

interface LikeAPI {
    /**
     * 결재서류 좋아요 누른 유저 목록 조회
     */
    @GET("/likes")
    @Headers("content-type: application/json")
    fun getLikeUsers(
        @Header("Authorization") accessToken: String,
        @Query("documentId") documentId: Int
    ): Call<UserListDto>

    /**
     * LikeAPI
     * 유저 팔로우/언팔로우
     */
    @POST("/follow")
    @Headers("content-type: application/json")
    fun setFollowState(
        @Header("Authorization") accessToken: String,
        @Query("userId") userId: Int
    ): Call<FollowStateDto>
}