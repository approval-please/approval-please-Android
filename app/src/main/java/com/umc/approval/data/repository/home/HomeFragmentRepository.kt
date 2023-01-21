package com.umc.approval.data.repository.home

import com.umc.approval.data.dto.ApprovalPaperDto
import com.umc.approval.data.dto.CommunityPostDto
import com.umc.approval.data.dto.ApprovalReportDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.HomeApi
import retrofit2.Call

/**
 * Home Fragment Repository
 */
class HomeFragmentRepository {
    /**
     * 관심부서 결재서류 목록 조회 API
     */
    fun getInterestingCategoryDocuments(idToken: String, category: Int): Call<ApprovalPaperDto> {
        return HomeApi.getInterestingCategoryDocuments(idToken, category)
    }

    /**
     * 전체 부서 결재서류 목록 조회 API
     */
    fun getDocuments(idToken: String, sortBy: Int): Call<ApprovalPaperDto> {
        return HomeApi.getDocuments(idToken, sortBy)
    }

    /**
     * 인기 게시글 목록 조회 API
     */
    fun getHotPosts(idToken: String, sortBy: Int): Call<CommunityPostDto> {
        return HomeApi.getHotPosts(idToken, sortBy)
    }

    /**
     * 결재 보고서 목록 조회 API
     */
    fun getReports(idToken: String): Call<ApprovalReportDto> {
        return HomeApi.getReports(idToken)
    }

}