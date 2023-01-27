package com.umc.approval.ui.viewmodel.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.login.get.ReturnPhoneAuthDto
import com.umc.approval.data.dto.login.get.ReturnPhoneAuthRequestDto
import com.umc.approval.data.dto.login.post.PhoneAuthDto
import com.umc.approval.data.repository.login.LoginFragmentRepository
import kotlinx.coroutines.launch
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
                    if (response.body()!!.isDuplicate == true) { //만약 폰넘버가 중복되지 않으면
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
}