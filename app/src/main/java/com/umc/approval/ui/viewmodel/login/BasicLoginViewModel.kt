package com.umc.approval.ui.viewmodel.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.login.get.ReturnBasicLoginDto
import com.umc.approval.data.dto.login.get.ReturnPhoneAuthDto
import com.umc.approval.data.dto.login.get.ReturnPhoneAuthRequestDto
import com.umc.approval.data.dto.login.post.BasicJoinDto
import com.umc.approval.data.dto.login.post.BasicLoginDto
import com.umc.approval.data.dto.login.post.PhoneAuthDto
import com.umc.approval.data.repository.login.LoginFragmentRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Basic Login ViewModel
 * */
class BasicLoginViewModel() : ViewModel() {

    private val repository = LoginFragmentRepository()

    private var _accessToken = MutableLiveData<String>()
    val accessToken : LiveData<String>
        get() = _accessToken

    /**
     * 로그인 성공시 엑세스 토큰 발급
     * */
    fun login(basicLoginDto: BasicLoginDto) = viewModelScope.launch {
        val response = repository.basic_login(basicLoginDto.email, basicLoginDto.password)
        response.enqueue(object : Callback<ReturnBasicLoginDto> {
            override fun onResponse(call: Call<ReturnBasicLoginDto>, response: Response<ReturnBasicLoginDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    //엑세스 토큰 저장
//                    _accessToken.value = response.headers().get("Authorization").toString()
//                    Log.d("return_accessToken", accessToken.value.toString())
//                    setAccessToken(accessToken.value.toString())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ReturnBasicLoginDto>, t: Throwable) {
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
}