package com.umc.approval.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.ui.dataStore.AccessTokenDataStore
import com.umc.approval.ui.repository.LoginFragmentRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragmentViewModel() : ViewModel() {

    private val repository = LoginFragmentRepository()

    private var _accessToken = MutableLiveData<String>()
    val accessToken : LiveData<String>
        get() = _accessToken

    /**
     * 로그인 성공시 엑세스 토큰 발급
     * */
    fun login(idToken: String, case: String) = viewModelScope.launch {
        val response = repository.login(idToken, case)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _accessToken.value = response.headers().get("Authorization").toString()
                    Log.d("return_accessToken", accessToken.value.toString())
                    setAccessToken(accessToken.value.toString())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**
     * 엑세스 토큰 가져와 검증까지 해결
     * */
    fun checkAccessToken() = viewModelScope.launch {
        val tokenValue = AccessTokenDataStore().getAccessToken().first()
        Log.d("getTokenValue", tokenValue)

        val response =  repository.connectServer(tokenValue)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", "SUCCESS")
                    _accessToken.value = response.headers().get("Authorization").toString()
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**
     * 로그인 시 엑세스 토큰 저장
     * */
    fun setAccessToken(token : String) = viewModelScope.launch {
        Log.d("setTokenValue", token)
        AccessTokenDataStore().setAccessToken(token)
    }

    /**
     * 엑세스 토큰 삭제 메소드
     * */
    fun deleteAccessToken() = viewModelScope.launch {
        AccessTokenDataStore().deleteAccessToken()
        _accessToken.value = ""
    }
}