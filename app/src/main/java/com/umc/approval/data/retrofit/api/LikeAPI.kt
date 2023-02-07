package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.approval.get.LikeReturnDto
import com.umc.approval.data.dto.approval.post.LikeDto
import com.umc.approval.data.dto.follow.FollowStateDto
import com.umc.approval.data.dto.common.CommonUserListDto
import retrofit2.Call
import retrofit2.http.*

interface LikeAPI {

    @POST("/likes")
    @Headers("content-type: application/json")
    fun like(
        @Header("Authorization") accessToken: String, @Body likeDto: LikeDto
    ): Call<LikeReturnDto>

    /**
     * @Post
     * accessToken: 사용자 검증 토큰
     * documentId: 결재 서류 ID
     * @Get
     * UserListDto: 유저 정보(uesrId, profileImage, nickname, level, isFollow) 리스트
     * 결재서류 좋아요 목록 조회 API
     */
    @GET("/likes")
    @Headers("content-type: application/json")
    fun getPaperLikeUsers(
        @Header("Authorization") accessToken: String ?= null,
        @Query("documentId") documentId: Int
    ): Call<CommonUserListDto>

    /**
     * @Post
     * accessToken: 사용자 검증 토큰
     * toktokId: 결재 톡톡 게시글 id
     * @Get
     * UserListDto: 유저 정보(userId, profileImage, nickname, level, isFollow) 리스트
     * 결재톡톡 좋아요 목록 조회 API
     */
    @GET("/likes")
    @Headers("content-type: application/json")
    fun getToktokLikeUsers(
        @Header("Authorization") accessToken: String ?= null,
        @Query("toktokId") toktokId: Int
    ): Call<CommonUserListDto>

    /**
     * @Post
     * accessToken: 사용자 검증 토큰
     * reportId: 결재보고서 id
     * @Get
     * UserListDto: 유저 정보(userId, profileImage, nickname, level, isFollow) 리스트
     * 결재보고서 좋아요 목록 조회 API
     */
    @GET("/likes")
    @Headers("content-type: application/json")
    fun getReportLikeUsers(
        @Header("Authorization") accessToken: String ?= null,
        @Query("reportId") reportId: Int
    ): Call<CommonUserListDto>

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