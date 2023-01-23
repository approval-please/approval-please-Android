package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.follow.FollowStateDto
import com.umc.approval.data.dto.common.CommonUserListDto
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
    ): Call<CommonUserListDto>

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
    ): Call<CommonUserListDto>
}