package com.umc.approval.data.repository.profile

import com.umc.approval.data.dto.profile.ProfileDto
import com.umc.approval.data.retrofit.api.ProfileAPI
import com.umc.approval.data.retrofit.instance.RetrofitInstance.profileApi
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * ProfileChange Fragment Repository
 * */
class ProfileChangeFragmentRepository() {

    /**
     * 프로필 변경 API
     * */
    fun profile_change(accessToken: String, profile: ProfileDto):Call<ResponseBody> {
        return profileApi.profile_change(accessToken, profile)
    }

    /**
     * 프로필 정보 로드 API
     * */
    fun profile_get(accessToken: String):Call<ProfileDto>{
        return profileApi.profile_get(accessToken)
    }
}