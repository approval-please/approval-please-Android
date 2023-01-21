package com.umc.approval.data.repository.like

import com.umc.approval.data.dto.FollowStateDto
import com.umc.approval.data.dto.UserListDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.LikeApi
import retrofit2.Call

/**
 * Like Activity Repository
 */
class LikeActivityRepository {
    /**
     * 결재 서류 좋아요 목록 조회 API
     */
    fun getLikeUsers(idToken: String, documentId: Int): Call<UserListDto> {
        return LikeApi.getLikeUsers(idToken, documentId)
    }

    /**
     * 유저 팔로우/언팔로우 API
     */
    fun setFollowState(idToken: String, userId: Int): Call<FollowStateDto> {
        return LikeApi.setFollowState(idToken, userId)
    }
}