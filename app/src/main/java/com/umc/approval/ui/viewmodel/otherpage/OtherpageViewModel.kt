package com.umc.approval.ui.viewmodel.otherpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.profile.ProfileDto
import com.umc.approval.data.repository.otherpage.OtherPageActivityRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OtherpageViewModel() : ViewModel() {
    private val repository = OtherPageActivityRepository()

    /* 다른 사람 프로필 라이브 데이터*/
    private var _othersInfo = MutableLiveData<ProfileDto>()
    val othersInfo : LiveData<ProfileDto>
    get() = _othersInfo

    /* 다른 사람 프로필 조회 */
    fun other_profile(userId : Int) = viewModelScope.launch {
        val response = repository.get_other_page(userId)
        response.enqueue(object : Callback<ProfileDto>{
            override fun onResponse(call: Call<ProfileDto>, response: Response<ProfileDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _othersInfo.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ProfileDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /* 프로필 조회 테스트 데이터 */
    fun init_other_profile() = viewModelScope.launch {
        val test = ProfileDto("닉네임", "", "소개", 100, 0, 10, 10, true)
        _othersInfo.postValue(test)
    }
}