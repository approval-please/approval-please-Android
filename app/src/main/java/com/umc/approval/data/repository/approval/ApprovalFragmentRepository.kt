package com.umc.approval.data.repository.approval

import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.ApprovalApi
import retrofit2.Call

/**
 * Approval Fragment Repository
 */
class ApprovalFragmentRepository {
    /**
     * 결재서류 목록 조회 API
     */
    fun getDocuments(accessToken: String, category: Int, sortBy: Int, state: Int): Call<ApprovalPaperDto> {
        return ApprovalApi.getDocuments(accessToken, category, sortBy, state)
    }

    /**
     * 관심부서 결재서류 목록 조회 API
     */
    fun getInterestingCategoryDocuments(idToken: String, category: Int, sortBy: Int, state: Int): Call<ApprovalPaperDto> {
        return ApprovalApi.getInterestingCategoryDocuments(idToken, category, sortBy, state)
    }

    /**
     * 테스트
     */
    fun test(approval : ApprovalPaperDto): Call<ApprovalPaperDto> {
        return ApprovalApi.test(approval)
    }
}