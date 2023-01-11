package com.umc.approval.ui.repository

import android.util.Log
import com.umc.approval.ui.dto.UserDto
import com.umc.approval.ui.retrofit.instance.RetrofitInstance.serverApi
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Login Fragment Repository
 * */
class LoginFragmentRepository() {

    /**
     * LOGIN API
     * Id, Access 토큰이나 임시코드를 보내는 API 호출
     * */
    fun login(idToken: String, case: String): Call<ResponseBody> {
        return serverApi.login(idToken,case)
    }

    /**
     * 서버와 연결 API
     * */
    fun connectServer(accessToken: String): Call<ResponseBody> {
        return serverApi.connectServer(accessToken)
    }

    /**
     * 사용자 정보 조회 API
     * */
    fun userInfo(accessToken: String): Call<UserDto> {
        return serverApi.userInfo(accessToken)
    }
}