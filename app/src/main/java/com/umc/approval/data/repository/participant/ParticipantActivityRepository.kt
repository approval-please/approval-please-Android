package com.umc.approval.data.repository.participant

import com.umc.approval.data.dto.FollowStateDto
import com.umc.approval.data.dto.UserListDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.ParticipantApi
import retrofit2.Call

/**
 * Participant Activity Repository
 */
class ParticipantActivityRepository {
    /**
     * 결재 서류 승인 유저 목록 조회 API
     */
    fun getApproveUsers(accessToken: String, documentId: Int): Call<UserListDto> {
        return ParticipantApi.getApproveUsers(accessToken, documentId)
    }

    /**
     * 결재 서류 반려 유저 목록 조회 API
     */
    fun getRejectUsers(accessToken: String, documentId: Int): Call<UserListDto> {
        return ParticipantApi.getRejectUsers(accessToken, documentId)
    }

    /**
     * 유저 팔로우/언팔로우 API
     */
    fun setFollowState(accessToken: String, userId: Int): Call<FollowStateDto> {
        return ParticipantApi.setFollowState(accessToken, userId)
    }
}