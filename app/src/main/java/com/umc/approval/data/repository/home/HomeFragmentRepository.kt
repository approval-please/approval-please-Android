package com.umc.approval.data.repository.home

import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.HomeApi
import retrofit2.Call

/**
 * Home Fragment Repository
 */
class HomeFragmentRepository {
    /**
     * 관심부서 결재서류 목록 조회 API
     */
    fun getInterestingCategoryDocuments(accessToken: String, state: Int): Call<ApprovalPaperDto> {
        return HomeApi.getInterestingCategoryDocuments(accessToken, state)
    }

    /**
     * 전체 부서 결재서류 목록 조회 API
     */
    fun getDocuments(state: Int): Call<ApprovalPaperDto> {
        return HomeApi.getDocuments(state)
    }

    /**
     * 인기 게시글 목록 조회 API
     */
    fun getHotPosts(state: Int): Call<CommunityTokDto> {
        return HomeApi.getHotPosts(state)
    }

    /**
     * 결재 보고서 목록 조회 API
     */
    fun getReports(state: Int): Call<CommunityReportDto> {
        return HomeApi.getReports(state)
    }

}