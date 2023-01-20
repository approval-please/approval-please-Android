package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.FollowStateDto
import com.umc.approval.data.dto.UserListDto
import retrofit2.Call
import retrofit2.http.*

interface ParticipantAPI {
    /**
     * ParticipantAPI
     * 결재 서류 승인한 유저 목록 조회
     */
    @GET("/approve")
    @Headers("content-type: application/json")
    fun getApproveUsers(
        @Header("Authorization") accessToken: String,
        @Query("documentId") documentId: Int
    ): Call<UserListDto>

    /**
     * ParticipantAPI
     * 결재 서류 반려한 유저 목록 조회
     */
    @GET("/reject")
    @Headers("content-type: application/json")
    fun getRejectUsers(
        @Header("Authorization") accessToken: String,
        @Query("documentId") documentId: Int
    ): Call<UserListDto>

    /**
     * ParticipantAPI
     * 유저 팔로우/언팔로우
     */
    @POST("/follow")
    @Headers("content-type: application/json")
    fun setFollowState(
        @Header("Authorization") accessToken: String,
        @Query("userId") userId: Int
    ): Call<FollowStateDto>
}