package com.umc.approval.data.repository
import com.umc.approval.data.retrofit.instance.RetrofitInstance.accessTokenAPI
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * AccessToken Check Repository
 * 구현 완료
 * */
class AccessTokenRepository {

    fun checkAccessToken(accessToken: String): Call<ResponseBody> {
        return accessTokenAPI.checkAccessToken(accessToken)
    }
}