package com.umc.approval.data.repository.mypage

import com.umc.approval.data.dto.mypage.FollowListDto
import com.umc.approval.data.dto.mypage.Profile
import com.umc.approval.data.dto.profile.ProfileChange
import com.umc.approval.data.dto.profile.ProfileDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.mypageAPI
import okhttp3.ResponseBody
import retrofit2.Call

class MyPageFragmentRepository {

    fun get_my_page(accessToken : String) : Call<ProfileDto>{
        return mypageAPI.get_my_page(accessToken)
    }

    fun get_my_follower(accessToken : String) : Call<FollowListDto>{
        return mypageAPI.get_my_followers(accessToken)
    }

    fun get_my_followings(accessToken : String) : Call<FollowListDto>{
        return mypageAPI.get_my_followings(accessToken)
    }

    fun change_my_profile(accessToken : String, profileChange: ProfileChange) : Call<ResponseBody>{
        return mypageAPI.change_my_profile(accessToken, profileChange)
    }
}