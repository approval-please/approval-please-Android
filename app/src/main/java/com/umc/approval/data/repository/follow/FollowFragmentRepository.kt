package com.umc.approval.data.repository.follow

import com.umc.approval.data.dto.follow.FollowerDto
import com.umc.approval.data.dto.follow.FollowingDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.followApi
import retrofit2.Call

class FollowFragmentRepository() {

    fun get_follower(accessToken : String) : Call<List<FollowerDto>>{
        return followApi.get_follower(accessToken)
    }

    fun get_following(accessToken : String) : Call<List<FollowingDto>>{
        return followApi.get_following(accessToken)
    }
}