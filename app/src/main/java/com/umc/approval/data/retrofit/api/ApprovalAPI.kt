package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.ApprovalPaperDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApprovalAPI {
    /**
     * ApprovalAPI
     * 결재서류 목록 조회
     */
    @GET("/documents")
    @Headers("content-type: application/json")
    fun getDocuments(
        @Header("Authorization") accessToken: String,
        @Query("category") category: Int,
        @Query("sortBy") sortBy: Int,  // 정렬 방식(인기, 최신)
        @Query("state") state: Int,  // 승인 상태 필터링(승인됨, 반려됨, 승인대기중)
    ): Call<ApprovalPaperDto>

    /**
     * ApprovalAPI
     * 관심부서 결재서류 목록 조회
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