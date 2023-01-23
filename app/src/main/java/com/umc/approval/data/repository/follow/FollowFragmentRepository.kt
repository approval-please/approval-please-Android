package com.umc.approval.data.repository.follow

import com.umc.approval.data.dto.common.CommonUserListDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.followApi
import retrofit2.Call

class FollowFragmentRepository() {

    fun get_follower(accessToken : String) : Call<CommonUserListDto>{
        return followApi.get_follower(accessToken)
    }

    fun get_following(accessToken : String) : Call<CommonUserListDto>{
        return followApi.get_following(accessToken)
    }
}