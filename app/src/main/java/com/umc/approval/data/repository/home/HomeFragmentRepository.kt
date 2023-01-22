package com.umc.approval.data.repository.home

import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
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
    fun getInterestingCategoryDocuments(accessToken: String, category: Int): Call<ApprovalPaperDto> {
        return HomeApi.getInterestingCategoryDocuments(accessToken, category)
    }

    /**
     * 전체 부서 결재서류 목록 조회 API
     */
    fun getDocuments(sortBy: Int): Call<ApprovalPaperDto> {
        return HomeApi.getDocuments(sortBy)
    }

    /**
     * 인기 게시글 목록 조회 API
     */
    fun getHotPosts(sortBy: Int): Call<CommunityPostDto> {
        return HomeApi.getHotPosts(sortBy)
    }

    /**
     * 결재 보고서 목록 조회 API
     */
    fun getReports(): Call<ApprovalReportDto> {
        return HomeApi.getReports()
    }

}