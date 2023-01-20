package com.umc.approval.data.repository.community

import com.umc.approval.data.dto.community.CommunityItemsDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.communityApi
import retrofit2.Call

/**
 * Community Repository
 * */
class CommunityRepository() {

    /**get approval items*/
    fun get_approval_items(accessToken: String):Call<CommunityItemsDto> {
        return communityApi.get_community_items(accessToken)
    }
}