package com.umc.approval.ui.viewmodel.like

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.common.CommonUserListDto
import com.umc.approval.data.repository.AccessTokenRepository
import com.umc.approval.data.repository.like.LikeRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikeViewModel(): ViewModel() {

    /**Like repository*/
    private val repository = LikeRepository()

    //엑세스 토큰 체크 리포지토리
    private val accessTokenRepository = AccessTokenRepository()

    //엑세스 토큰이 존재할시 True값 지정, True 시 로그인 상태
    private var _accessToken = MutableLiveData<Boolean>()
    val accessToken : LiveData<Boolean>
        get() = _accessToken

    // 결재서류, 결재톡톡, 결재보고서 id
    private var _id = MutableLiveData<Int>()
    val id: LiveData<Int>
        get() = _id

    // 좋아요 누른 유저 목록
    private var _likeList = MutableLiveData<CommonUserListDto>()
    val likeList: LiveData<CommonUserListDto>
        get() = _likeList

    /**
     * 결재 서류 좋아요 목록 조회
     */
    fun get_paper_like_users(id: Int) = viewModelScope.launch {

        if (accessToken.value!!) {  // 로그인 상태일 때
            val accessToken = AccessTokenDataStore().getAccessToken().first()

            val response = repository.getPaperLikeUsers(accessToken, id)
            response.enqueue(object : Callback<CommonUserListDto> {
                override fun onResponse(call: Call<CommonUserListDto>, response: Response<CommonUserListDto>) {
                    if (response.isSuccessful) {
                        Log.d("RESPONSE", response.body().toString())
                        _likeList.postValue(response.body())
                    } else {
                        Log.d("RESPONSE", "FAIL")
                    }
                }
                override fun onFailure(call: Call<CommonUserListDto>, t: Throwable) {
                    Log.d("ContinueFail", "FAIL")
                }
            })
        } else {  // 로그인하지 않은 상태일 때
            val response = repository.getPaperLikeUsers(id)
            response.enqueue(object : Callback<CommonUserListDto> {
                override fun onResponse(call: Call<CommonUserListDto>, response: Response<CommonUserListDto>) {
                    if (response.isSuccessful) {
                        Log.d("RESPONSE", response.body().toString())
                        _likeList.postValue(response.body())
                    } else {
                        Log.d("RESPONSE", "FAIL")
                    }
                }
                override fun onFailure(call: Call<CommonUserListDto>, t: Throwable) {
                    Log.d("ContinueFail", "FAIL")
                }
            })
        }
    }

    /**
     * 결재 톡톡 좋아요 목록 조회
     */
    fun get_toktok_like_users(id: Int) = viewModelScope.launch {
        if (accessToken.value!!) {
            val accessToken = AccessTokenDataStore().getAccessToken().first()

            val response = repository.getToktokLikeUsers(accessToken, id)
            response.enqueue(object : Callback<CommonUserListDto> {
                override fun onResponse(call: Call<CommonUserListDto>, response: Response<CommonUserListDto>) {
                    if (response.isSuccessful) {
                        Log.d("RESPONSE", response.body().toString())
                        _likeList.postValue(response.body())
                    } else {
                        Log.d("RESPONSE", "FAIL")
                    }
                }
                override fun onFailure(call: Call<CommonUserListDto>, t: Throwable) {
                    Log.d("ContinueFail", "FAIL")
                }
            })
        } else {
            val response = repository.getToktokLikeUsers(id)
            response.enqueue(object : Callback<CommonUserListDto> {
                override fun onResponse(call: Call<CommonUserListDto>, response: Response<CommonUserListDto>) {
                    if (response.isSuccessful) {
                        Log.d("RESPONSE", response.body().toString())
                        _likeList.postValue(response.body())
                    } else {
                        Log.d("RESPONSE", "FAIL")
                    }
                }
                override fun onFailure(call: Call<CommonUserListDto>, t: Throwable) {
                    Log.d("ContinueFail", "FAIL")
                }
            })
        }

    }

    /**
     * 결재 보고서 좋아요 목록 조회
     */
    fun get_report_like_users(id: Int) = viewModelScope.launch {
        if (accessToken.value!!) {
            val accessToken = AccessTokenDataStore().getAccessToken().first()

            val response = repository.getReportLikeUsers(accessToken, id)
            response.enqueue(object : Callback<CommonUserListDto> {
                override fun onResponse(call: Call<CommonUserListDto>, response: Response<CommonUserListDto>) {
                    if (response.isSuccessful) {
                        Log.d("RESPONSE", response.body().toString())
                        _likeList.postValue(response.body())
                    } else {
                        Log.d("RESPONSE", "FAIL")
                    }
                }
                override fun onFailure(call: Call<CommonUserListDto>, t: Throwable) {
                    Log.d("ContinueFail", "FAIL")
                }
            })
        } else {
            val response = repository.getReportLikeUsers(id)
            response.enqueue(object : Callback<CommonUserListDto> {
                override fun onResponse(call: Call<CommonUserListDto>, response: Response<CommonUserListDto>) {
                    if (response.isSuccessful) {
                        Log.d("RESPONSE", response.body().toString())
                        _likeList.postValue(response.body())
                    } else {
                        Log.d("RESPONSE", "FAIL")
                    }
                }
                override fun onFailure(call: Call<CommonUserListDto>, t: Throwable) {
                    Log.d("ContinueFail", "FAIL")
                }
            })
        }
    }

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
}