package com.umc.approval.ui.viewmodel.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.login.get.ReturnBasicLoginDto
import com.umc.approval.data.dto.login.post.BasicLoginDto
import com.umc.approval.data.dto.login.post.PasswordChangeDto
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
class PasswordChangeViewModel() : ViewModel() {

    private val repository = LoginFragmentRepository()

    /**
     * 로그인 성공시 엑세스 토큰 발급
     * */
    fun password_change(passwordChangeDto: PasswordChangeDto) = viewModelScope.launch {
        val response = repository.password_change(passwordChangeDto)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
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