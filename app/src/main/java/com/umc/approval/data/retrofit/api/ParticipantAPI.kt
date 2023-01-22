package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.FollowStateDto
import com.umc.approval.data.dto.UserListDto
import retrofit2.Call
import retrofit2.http.*

interface ParticipantAPI {
    /**
     * @Post
     * accessToken: 사용자 검증 토큰
     * documentId: 결재 서류 ID
     * @Get
     * UserListDto: 유저 정보(profileImage, nickname, level, isFollow) 리스트
     * 결재 서류 승인 유저 목록 조회 API
     */
    @GET("/approve")
    @Headers("content-type: application/json")
    fun getApproveUsers(
        @Header("Authorization") accessToken: String,
        @Query("documentId") documentId: Int
    ): Call<UserListDto>

    /**
     * @Post
     * accessToken: 사용자 검증 토큰
     * documentId: 결재 서류 ID
     * @Get
     * UserListDto: 유저 정보(profileImage, nickname, level, isFollow) 리스트
     * 결재 서류 반려 유저 목록 조회 API
     */
    @GET("/reject")
    @Headers("content-type: application/json")
    fun getRejectUsers(
        @Header("Authorization") accessToken: String,
        @Query("documentId") documentId: Int
    ): Call<UserListDto>

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
    fun setFollowState(
        @Header("Authorization") accessToken: String,
        @Query("userId") userId: Int
    ): Call<FollowStateDto>
}