package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApprovalAPI {

    /**
     * @Post
     * accessToken: 사용자 검증 토큰
     * category: 사용자가 선택한 카테고리
     * sortBy: 정렬 방식(최신순/정확도 순)
     * state: 승인 상태(상태 전체/승인됨/반려됨/결재 대기중)
     * @Get
     * ApprovalPaperDto: 결재 서류(state, category, updatedAt, image, title, content, tag, viewCount, approveCount, rejectCount 정보) 리스트
     * 결재서류 목록 조회 API
     */
    @GET("/documents")
    @Headers("content-type: application/json")
    fun getDocuments(
        @Header("Authorization") accessToken: String,
        @Query("category") category: Int,
        @Query("sortBy") sortBy: Int,
        @Query("state") state: Int,
    ): Call<ApprovalPaperDto>

    /**
     * @Post
     * accessToken: 사용자 검증 토큰
     * category: 관심부서 중 사용자가 선택한 카테고리
     * sortBy: 정렬 방식(최신순/정확도 순)
     * state: 승인 상태(상태 전체/승인됨/반려됨/결재 대기중)
     * @Get
     * ApprovalPaperDto: 결재 서류(state, category, updatedAt, image, title, content, tag, viewCount, approveCount, rejectCount 정보) 리스트
     * 관심부서 결재서류 목록 조회 API
     */
    @GET("/documents/likes")
    @Headers("content-type: application/json")
    fun getInterestingCategoryDocuments(
        @Header("Authorization") accessToken: String,
        @Query("category") category: Int,
        @Query("sortBy") sortBy: Int,
        @Query("state") state: Int,
    ): Call<ApprovalPaperDto>
}