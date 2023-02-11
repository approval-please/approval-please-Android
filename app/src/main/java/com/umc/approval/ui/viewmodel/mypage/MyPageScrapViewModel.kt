package com.umc.approval.ui.viewmodel.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.community.get.*
import com.umc.approval.data.dto.mypage.MyScrapDto
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.data.repository.mypage.MyPageFragmentRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/* 내 정보 스크랩 탭 데이터 ViewModel */
class MyPageScrapViewModel() : ViewModel() {
    private val repository = MyPageFragmentRepository()

    /* 댓글 탭 라이브 데이터 */
    private var _scrap = MutableLiveData<MyScrapDto>()
    val scrap : LiveData<MyScrapDto>
        get() = _scrap

    /* 스크랩 목록 조회 */
    fun get_my_scraps(postType : Int?=null, state: Int?=null) = viewModelScope.launch {
        val accessToken = AccessTokenDataStore().getAccessToken().first()
        val response = repository.get_my_scraps(accessToken, postType, state)
        response.enqueue(object : Callback<MyScrapDto> {
            override fun onResponse(call: Call<MyScrapDto>, response: Response<MyScrapDto>) {
                if(response.isSuccessful){
                    _scrap.postValue(response.body())
                    Log.d("RESPONSE", response.body().toString())
                }
                else{
                    Log.d("RESPONSE", "FAIL")
                }
            }

            override fun onFailure(call: Call<MyScrapDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}