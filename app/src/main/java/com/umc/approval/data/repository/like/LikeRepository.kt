package com.umc.approval.data.repository.like

import com.umc.approval.data.dto.approval.get.LikeReturnDto
import com.umc.approval.data.dto.approval.post.LikeDto
import com.umc.approval.data.dto.common.CommonUserListDto
import com.umc.approval.data.dto.follow.FollowStateDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.LikeApi
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Like Activity Repository
 */
class LikeRepository {

    //검사 완료
    fun like(accessToken: String, likeDto: LikeDto): Call<LikeReturnDto> {
        return LikeApi.like(accessToken, likeDto)
    }

    /**
     * 결재 서류 좋아요 목록 조회 API
     */
    fun getPaperLikeUsers(accessToken: String, documentId: Int): Call<CommonUserListDto> {
        return LikeApi.getPaperLikeUsers(accessToken, documentId)
    }

    fun getPaperLikeUsers(documentId: Int): Call<CommonUserListDto> {
        return LikeApi.getPaperLikeUsers(documentId)
    }

    /**
     * 결재 톡톡 좋아요 목록 조회 API
     */
    fun getToktokLikeUsers(accessToken: String, toktokId: Int): Call<CommonUserListDto> {
        return LikeApi.getToktokLikeUsers(accessToken, toktokId)
    }

    fun getToktokLikeUsers(toktokId: Int): Call<CommonUserListDto> {
        return LikeApi.getToktokLikeUsers(toktokId)
    }

    /**
     * 결재보고서 좋아요 목록 조회 API
     */
    fun getReportLikeUsers(accessToken: String, reportId: Int): Call<CommonUserListDto> {
        return LikeApi.getReportLikeUsers(accessToken, reportId)
    }

    fun getReportLikeUsers(reportId: Int): Call<CommonUserListDto> {
        return LikeApi.getReportLikeUsers(reportId)
    }

    /**
     * 유저 팔로우/언팔로우 API
     */
    fun setFollowState(accessToken: String, userId: Int): Call<FollowStateDto> {
        return LikeApi.setFollowState(accessToken, userId)
    }
}