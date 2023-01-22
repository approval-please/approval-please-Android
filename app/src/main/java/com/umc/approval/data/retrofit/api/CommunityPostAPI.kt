package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.arankimDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface CommunityPostAPI {
    /**
     * @Get
     * accessToken: 사용자 검증 토큰
     * postID: 커뮤니티 포스트 ID
     * @Get
     * CommunityPostDto: 커뮤니티 포스트 정보(profileImage, nickname, level, isFollow) 리스트
     * 결재서류 좋아요 목록 조회 API
     */
    @GET("/community")
    @Headers("content-type: application/json")
    fun getCommunityPost(
        @Header("Authorization") accessToken: String,
        @Query("postId") postId: Int
    ): Call<arankimDto>

}