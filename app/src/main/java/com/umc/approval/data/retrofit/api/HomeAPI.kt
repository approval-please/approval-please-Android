package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.ApprovalPaperDto
import com.umc.approval.data.dto.CommunityPostDto
import com.umc.approval.data.dto.ApprovalReportDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface HomeAPI {
    /**
     * HomeAPI
     * 관심부서 결재서류 목록 조회 (내 관심부서 서류)
     */
    @GET("/documents/likes")
    @Headers("content-type: application/json")
    fun getInterestingCategoryDocuments(
        @Header("Authorization") accessToken: String,
        @Query("category") category: Int
    ): Call<ApprovalPaperDto>

    /**
     * HomeAPI
     * 결재서류 목록 조회 (결재서류 검토하기)
     */
    @GET("/documents")
    @Headers("content-type: application/json")
    fun getDocuments(
        @Header("Authorization") accessToken: String,
        @Query("sortBy") sortBy: Int,  // 정렬 방식(인기, 최신)
    ): Call<ApprovalPaperDto>

    /**
     * HomeAPI
     * 인기 게시글 목록 조회 -> 게시글 목록 조회에서 인기순 정렬로 가져오면 될 듯
     */
    @GET("/community/toktok")
    @Headers("content-type: application/json")
    fun getHotPosts(
        @Header("Authorization") accessToken: String,
        @Query("sortBy") sortBy: Int
    ): Call<CommunityPostDto>

    /**
     * HomeAPI
     * 결재보고서 목록 조회
     */
    @GET("/community/reports")
    @Headers("content-type: application/json")
    fun getReports(
        @Header("Authorization") accessToken: String
    ): Call<ApprovalReportDto>
}