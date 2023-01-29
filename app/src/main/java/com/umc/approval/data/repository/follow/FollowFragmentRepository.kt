package com.umc.approval.data.repository.follow

import com.umc.approval.data.dto.common.CommonUserListDto
import com.umc.approval.data.dto.follow.FollowStateDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.followApi
import retrofit2.Call

class FollowFragmentRepository() {

    fun get_follower(accessToken : String, query: String) : Call<CommonUserListDto>{
        return followApi.get_follower(accessToken, query)
    }

    fun get_following(accessToken : String, query: String) : Call<CommonUserListDto>{
        return followApi.get_following(accessToken, query)
    }

    fun follow(accessToken : String, userId: Int) : Call<FollowStateDto>{
        return followApi.follow(accessToken, userId)
    }
}