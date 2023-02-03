package com.umc.approval.data.repository.follow

import com.umc.approval.data.dto.approval.post.LikeDto
import com.umc.approval.data.dto.follow.FollowStateDto
import com.umc.approval.data.dto.follow.ScrapStateDto
import com.umc.approval.data.dto.mypage.FollowListDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.followApi
import retrofit2.Call

class FollowFragmentRepository() {

    //팔로우
    fun follow(accessToken : String, userId: Int) : Call<FollowStateDto>{
        return followApi.follow(accessToken, userId)
    }

    //스크랩
    fun scrap(accessToken : String, likeDto: LikeDto) : Call<ScrapStateDto>{
        return followApi.scrap(accessToken, likeDto)
    }

    fun get_my_follower(accessToken : String) : Call<FollowListDto>{
        return followApi.get_my_followers(accessToken)
    }

    fun get_my_followings(accessToken : String) : Call<FollowListDto>{
        return followApi.get_my_followings(accessToken)
    }
}