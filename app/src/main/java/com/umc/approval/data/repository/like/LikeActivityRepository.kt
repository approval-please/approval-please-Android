package com.umc.approval.data.repository.like

import com.umc.approval.data.dto.common.CommonUserListDto
import com.umc.approval.data.dto.follow.FollowStateDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.LikeApi
import retrofit2.Call

/**
 * Like Activity Repository
 */
class LikeActivityRepository {
    /**
     * 결재 서류 좋아요 목록 조회 API
     */
    fun getLikeUsers(accessToken: String, documentId: Int): Call<CommonUserListDto> {
        return LikeApi.getLikeUsers(accessToken, documentId)
    }

    /**
     * 유저 팔로우/언팔로우 API
     */
    fun setFollowState(accessToken: String, userId: Int): Call<FollowStateDto> {
        return LikeApi.setFollowState(accessToken, userId)
    }
}