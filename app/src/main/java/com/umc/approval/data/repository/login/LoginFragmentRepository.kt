package com.umc.approval.data.repository.login

import com.umc.approval.data.retrofit.instance.RetrofitInstance.serverApi
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Login Fragment Repository
 * */
class LoginFragmentRepository() {

    /**
     * Social Login API
     * */
    fun login(idToken: String, case: String): Call<ResponseBody> {
        return serverApi.social_login(idToken,case)
    }

    /**
     * Basic Login API
     * */
    fun basic_login(email: String, password: String): Call<ResponseBody> {
        return serverApi.basic_login(email, password)
    }

    /**
     * Connect to Server API
     * */
    fun connectServer(accessToken: String): Call<ResponseBody> {
        return serverApi.connect_server(accessToken)
    }
}