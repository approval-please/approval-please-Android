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
import com.umc.approval.data.dto.login.post.PhoneAuthDto
import com.umc.approval.data.dto.login.post.SocialJoinDto
import com.umc.approval.data.repository.login.LoginFragmentRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Email Check ViewModel
 * */
class JoinViewModel() : ViewModel() {

    private val repository = LoginFragmentRepository()

    /**인증 받았는지 상태 확인하는 라이브 데이터*/
    private var _phone = MutableLiveData<Boolean>()
    val phone : LiveData<Boolean>
        get() = _phone

    /**인증 받았는지 상태 확인하는 라이브 데이터*/
    private var _phone_auth = MutableLiveData<Int>()
    val phone_auth : LiveData<Int>
        get() = _phone_auth

    /**인증 받았는지 상태 확인하는 라이브 데이터*/
    private var _join_state = MutableLiveData<Boolean>()
    val join_state : LiveData<Boolean>
        get() = _join_state

    /**phone_auth 초기화*/
    fun phone_auth_to_zero() {
        _phone_auth.postValue(0)
    }

    /**
     * 휴대폰 눌렀을때 인증번호 반환하는 메서드
     * 정상 동작 Check 완료
     * */
    fun phone_request(phoneNumber: String) = viewModelScope.launch {

        val response = repository.phone_auth_request(phoneNumber)
        response.enqueue(object : Callback<ReturnPhoneAuthRequestDto> {
            override fun onResponse(call: Call<ReturnPhoneAuthRequestDto>, response: Response<ReturnPhoneAuthRequestDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _phone.postValue(true)
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ReturnPhoneAuthRequestDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**
     * 인증번호 입력했을때 체크하는 메서드
     * 정상 동작 Check 완료
     * */
    fun phone_auth_request(phoneAuthDto: PhoneAuthDto) = viewModelScope.launch {

        //초기화
        _phone_auth.postValue(0)

        val response = repository.phone_auth(phoneAuthDto)
        response.enqueue(object : Callback<ReturnPhoneAuthDto> {
            override fun onResponse(call: Call<ReturnPhoneAuthDto>, response: Response<ReturnPhoneAuthDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    if (response.body()!!.isDuplicate == false) { //만약 폰넘버가 중복되지 않으면
                        _phone_auth.postValue(1)
                    } else { //만약 폰넘버가 중복되면
                        _phone_auth.postValue(2)
                    }
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ReturnPhoneAuthDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**
     * 일반 회원가입
     * 정상 동작 Check 완료
     * */
    fun join(basicJoinDto: BasicJoinDto) = viewModelScope.launch {

        val response = repository.basic_join(basicJoinDto)
        response.enqueue(object : Callback<ReturnBasicLoginDto> {
            override fun onResponse(call: Call<ReturnBasicLoginDto>, response: Response<ReturnBasicLoginDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _join_state.postValue(true)
                    setAccessToken("Bearer " + response.body()!!.accessToken.toString())
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
     * 일반 회원가입
     * 정상 동작 Check 완료
     * */
    fun social_join(socialJoinDto: SocialJoinDto) = viewModelScope.launch {

        val response = repository.social_join(socialJoinDto)
        response.enqueue(object : Callback<ReturnBasicLoginDto> {
            override fun onResponse(call: Call<ReturnBasicLoginDto>, response: Response<ReturnBasicLoginDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _join_state.postValue(true)
//                    setAccessToken("Bearer " + response.body()!!.accessToken.toString())
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