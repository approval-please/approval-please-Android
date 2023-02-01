package com.umc.approval.ui.viewmodel.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.login.get.ReturnEmailCheckDto
import com.umc.approval.data.dto.login.get.ReturnSocialLoginDto
import com.umc.approval.data.repository.AccessTokenRepository
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
    private val accessTokenRepository = AccessTokenRepository()

    /**엑세스 토큰이 존재할시 True값 지정, True 시 로그인 상태*/
    private var _accessToken = MutableLiveData<Boolean>()
    val accessToken : LiveData<Boolean>
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

    /**소셜아이디 체크 라이브 데이터*/
    private var _social_id = MutableLiveData<String>()
    val social_id : LiveData<String>
        get() = _social_id

    /**엑세스 토큰 체크*/
    fun checkAccessToken() = viewModelScope.launch {
        val tokenValue = AccessTokenDataStore().getAccessToken().first()
        val response = accessTokenRepository.checkAccessToken(tokenValue)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", "Success")
                    _accessToken.postValue(true)
                } else {
                    Log.d("RESPONSE", "FAIL")
                    _accessToken.postValue(false)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**Kakao 소셜 로그인*/
    fun social_login(accessToken: String, email: String, code: String) = viewModelScope.launch {

        val response = repository.social_login("Bearer " + accessToken, email)
        _kakao_email.postValue(email)
        _social_id.postValue(code)

        response.enqueue(object : Callback<ReturnSocialLoginDto> {
            override fun onResponse(call: Call<ReturnSocialLoginDto>, response: Response<ReturnSocialLoginDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", "Success")
                    _social_check.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ReturnSocialLoginDto>, t: Throwable) {
                Log.d("ContinueFail", t.message.toString())
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
        _accessToken.value = false
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

    /**
     * 로그아웃
     * 정상 동작 Check 완료
     * */
    fun logout() = viewModelScope.launch {

        val tokenValue = AccessTokenDataStore().getAccessToken().first()

        val response = repository.logout(tokenValue)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    deleteAccessToken()
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}