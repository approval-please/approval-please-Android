package com.umc.approval.data.repository.follow

import com.umc.approval.data.dto.FollowDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.followApi
import retrofit2.Call

class FollowFragmentRepository() {
    // FollowFragment에 들어갈 구성 데이터를 가지고 오는 API 호출
    fun getFollowData(idToken : String) : Call<FollowDto>{
        return followApi.getFollowData(idToken)
    }
}