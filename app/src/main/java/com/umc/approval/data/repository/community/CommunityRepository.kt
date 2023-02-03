package com.umc.approval.data.repository.community

import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.data.dto.communityReport.get.CommunityReportDetailDto
import com.umc.approval.data.dto.communitydetail.get.CommunityItemDto
import com.umc.approval.data.dto.communitydetail.get.CommunityTokDetailDto
import com.umc.approval.data.dto.communitydetail.post.CommunityVotePost
import com.umc.approval.data.dto.communitydetail.post.CommunityVoteResult
import com.umc.approval.data.dto.upload.post.ReportUploadDto
import com.umc.approval.data.dto.upload.post.TalkUploadDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.communityApi
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Community Repository
 * */
class CommunityRepository() {

    /**get toktoks*/
    fun get_toks(accessToken: String, sortBy: Int ?= null):Call<CommunityTokDto> {
        return communityApi.get_community_tok_items(accessToken, sortBy)
    }

    /**get reports*/
    fun get_reports(accessToken: String, sortBy: Int ?= null):Call<CommunityReportDto> {
        return communityApi.get_community_report_items(accessToken, sortBy)
    }

    /**get toks*/
    fun tok_upload(accessToken: String, talkUploadDto: TalkUploadDto):Call<ResponseBody> {
        return communityApi.upload_community_tok(accessToken, talkUploadDto)
    }

    /**get reports*/
    fun report_upload(accessToken: String, reportUploadDto: ReportUploadDto):Call<ResponseBody> {
        return communityApi.upload_community_report(accessToken, reportUploadDto)
    }


    /**get toktok detail*/
    fun get_tok_detail(accessToken: String, tokId: String): Call<CommunityTokDetailDto> {
        return communityApi.get_community_tok_detail(accessToken, tokId)
    }

    /**get toktok detail*/
    fun post_vote(accessToken: String, voteId: String, list: CommunityVotePost): Call<CommunityVoteResult> {
        return communityApi.post_vote(accessToken, voteId, list)
    }

    /**delete toktok detail*/
    fun delete_tok(accessToken: String, tokId: String): Call<ResponseBody> {
        return communityApi.delete_tok(accessToken, tokId)
    }

    /**get report detail*/
    fun get_report_detail(accessToken: String, reportId: String): Call<CommunityReportDetailDto> {
        return communityApi.get_community_report_detail(accessToken, reportId)
    }

    /**get report detail*/
    fun delete_report(accessToken: String, reportId: String): Call<ResponseBody> {
        return communityApi.delete_report(accessToken, reportId)
    }
}