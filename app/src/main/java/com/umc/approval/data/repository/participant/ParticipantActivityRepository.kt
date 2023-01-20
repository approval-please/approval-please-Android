package com.umc.approval.data.repository.participant

import com.umc.approval.data.dto.FollowStateDto
import com.umc.approval.data.dto.UserListDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.ParticipantApi
import retrofit2.Call

class ParticipantActivityRepository {
    /**
     * 결재서류 승인한 유저 목록 조회 API
     */
    fun getApproveUsers(idToken: String, documentId: Int): Call<UserListDto> {
        return ParticipantApi.getApproveUsers(idToken, documentId)
    }

    /**
     * 결재서류 반려한 유저 목록 조회 API
     */
    fun getRejectUsers(idToken: String, documentId: Int): Call<UserListDto> {
        return ParticipantApi.getRejectUsers(idToken, documentId)
    }

    /**
     * 유저 팔로우/언팔로우 API
     */
    fun setFollowState(idToken: String, userId: Int): Call<FollowStateDto> {
        return ParticipantApi.setFollowState(idToken, userId)
    }
}