package com.umc.approval.data.repository.community

import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.community.get.CommunityTokDto
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
}