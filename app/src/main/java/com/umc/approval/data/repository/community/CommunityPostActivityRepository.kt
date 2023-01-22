package com.umc.approval.data.repository.community

import com.umc.approval.data.dto.arankimDto
import com.umc.approval.data.retrofit.api.CommunityPostAPI
import com.umc.approval.data.retrofit.instance.RetrofitInstance.CommunityPostAPI

import retrofit2.Call

class CommunityPostActivityRepository {


    fun getCommunityPost(accessToken: String, postId: Int): Call<arankimDto> {
        return CommunityPostAPI.getCommunityPost(accessToken, postId)
    }
}