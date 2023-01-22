package com.umc.approval.data.repository.community

import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.data.dto.communitydetail.get.CommunityItemDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.communityApi
import retrofit2.Call

/**
 * Community Repository
 * */
class CommunityRepository() {

    /**get toktoks*/
    fun get_toks(state: Int):Call<CommunityTokDto> {
        return communityApi.get_community_tok_items(state)
    }

    /**get reports*/
    fun get_reports(state: Int):Call<CommunityReportDto> {
        return communityApi.get_community_report_items(state)
    }

    /**get toktok detail*/
    fun get_tok_detail(accessToken: String, tokId: Int): Call<CommunityItemDto> {
        return communityApi.get_community_tok_detail(accessToken, tokId)
    }

    /**get report detail*/
    fun get_report_detail(accessToken: String, reportId: Int): Call<CommunityItemDto> {
        return communityApi.get_community_report_detail(accessToken, reportId)
    }


}