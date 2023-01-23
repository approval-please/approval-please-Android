package com.umc.approval.data.repository.mypage

import com.umc.approval.data.dto.mypage.MyPageDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.mypageAPI
import retrofit2.Call

class MyPageFragmentRepository {
    fun get_my_page(accessToken : String) : Call<MyPageDto>{
        return mypageAPI.get_my_page(accessToken)
    }
}