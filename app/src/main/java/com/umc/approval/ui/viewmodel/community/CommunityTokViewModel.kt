package com.umc.approval.ui.viewmodel.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.community.get.CommunityTok
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.data.repository.AccessTokenRepository
import com.umc.approval.data.repository.community.CommunityRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 로직 체크 완료
 * */
class CommunityTokViewModel() : ViewModel() {

    //커뮤니티 리포지토리
    private val repository = CommunityRepository()

    /**tok 목록 라이브 데이터*/
    private var _tok_list = MutableLiveData<CommunityTokDto>()
    val tok_list : LiveData<CommunityTokDto>
        get() = _tok_list

    /**
     * 모든 tok 목록을 반환받는 메소드
     * 정상 동작 Check 완료
     * */
    fun get_all_toks(sortBy: Int ?= null) = viewModelScope.launch {

        var sort = sortBy
        if (sort == 3) {
            sort = null
        }

        val response = repository.get_toks(sort)
        response.enqueue(object : Callback<CommunityTokDto> {
            override fun onResponse(call: Call<CommunityTokDto>, response: Response<CommunityTokDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _tok_list.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<CommunityTokDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}