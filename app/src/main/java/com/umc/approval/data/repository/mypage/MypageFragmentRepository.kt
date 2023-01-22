package com.umc.approval.data.repository.mypage

import com.umc.approval.data.dto.MyPageDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.mypageAPI
import retrofit2.Call

class MypageFragmentRepository {
    fun getMypage(idToken : String) : Call<MyPageDto>{
        return mypageAPI.getMyPage(idToken)
    }
}