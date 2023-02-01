package com.umc.approval.ui.viewmodel.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.mypage.CommunityDto
import com.umc.approval.data.repository.community.CommunityRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageCommunityViewModel() : ViewModel() {
    //결재 서류 리포지토리
    private val repository = CommunityRepository()

    /** 결재톡톡/보고서 목록 데이터*/
    private var _community = MutableLiveData<CommunityDto>()
    val community : LiveData<CommunityDto>
        get() = _community

    /**카테고리 선택 라이브 데이터*/
    private var _state = MutableLiveData<Int>()
    val state : LiveData<Int>
        get() = _state

    fun setState(int: Int) {
        _state.postValue(int)
    }

    fun get_community(postType: Int ?= null) = viewModelScope.launch {
        var pt = postType

        if (pt == 1) {
            pt = null
        }

        val accessToken = AccessTokenDataStore().getAccessToken().first()
        val response = repository.get_community(accessToken, pt)
        response.enqueue(object : Callback<CommunityDto> {
            override fun onResponse(call: Call<CommunityDto>, response: Response<CommunityDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _community.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<CommunityDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

}