package com.umc.approval.ui.viewmodel.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.login.get.ReturnEmailCheckDto
import com.umc.approval.data.dto.login.get.ReturnSocialLoginDto
import com.umc.approval.dataStore.AccessTokenDataStore
import com.umc.approval.data.repository.login.LoginFragmentRepository
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

    /**이메일 체크 라이브 데이터*/
    private var _email_check = MutableLiveData<ReturnEmailCheckDto>()
    val email_check : LiveData<ReturnEmailCheckDto>
        get() = _email_check

    /**소셜로그인 체크 라이브 데이터*/
    private var _social_check = MutableLiveData<ReturnSocialLoginDto>()
    val social_status : LiveData<ReturnSocialLoginDto>
        get() = _social_check

    /**소셜로그인 체크 라이브 데이터*/
    private var _kakao_email = MutableLiveData<String>()
    val kakao_email : LiveData<String>
        get() = _kakao_email

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
     * Kakao 소셜 로그인
     * */
    fun social_login(accessToken: String, email: String) = viewModelScope.launch {

        val response = repository.social_login("Bearer " + accessToken)
        _kakao_email.postValue(email)

        response.enqueue(object : Callback<ReturnSocialLoginDto> {
            override fun onResponse(call: Call<ReturnSocialLoginDto>, response: Response<ReturnSocialLoginDto>) {
                if (response.isSuccessful) {
                    _social_check.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ReturnSocialLoginDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**
     * 엑세스 토큰 가져와 검증까지 해결
     * */
    fun checkAccessToken() = viewModelScope.launch {
        val tokenValue = AccessTokenDataStore().getAccessToken().first()

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

    /**
     * 로그인 성공시 엑세스 토큰 발급
     * 정상 동작 Check 완료
     * */
    fun emailCheck(email : String) = viewModelScope.launch {

        val response = repository.email_check(email)
        response.enqueue(object : Callback<ReturnEmailCheckDto> {
            override fun onResponse(call: Call<ReturnEmailCheckDto>, response: Response<ReturnEmailCheckDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _email_check.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ReturnEmailCheckDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}