package com.umc.approval.ui.viewmodel.follow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.post.LikeDto
import com.umc.approval.data.dto.common.CommonUserDto
import com.umc.approval.data.dto.communitydetail.post.CommunityVoteResult
import com.umc.approval.data.dto.follow.FollowStateDto
import com.umc.approval.data.dto.follow.ScrapStateDto
import com.umc.approval.data.dto.mypage.FollowListDto
import com.umc.approval.data.repository.follow.FollowFragmentRepository
import com.umc.approval.data.repository.mypage.MyPageFragmentRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel() : ViewModel() {

    private val followRepository = FollowFragmentRepository()

    private var _followers = MutableLiveData<FollowListDto>()
    val followers : LiveData<FollowListDto>
        get() = _followers

    private var _followings = MutableLiveData<FollowListDto>()
    val followings : LiveData<FollowListDto>
        get() = _followings

    private var _isFollow = MutableLiveData<FollowStateDto>()
    val isFollow : LiveData<FollowStateDto>
        get() = _isFollow

    fun setFollow(li:FollowStateDto) {
        _isFollow.postValue(li)
    }

    private var _isScrap = MutableLiveData<ScrapStateDto>()
    val isScrap : LiveData<ScrapStateDto>
        get() = _isScrap

    fun setScrap(li:ScrapStateDto) {
        _isScrap.postValue(li)
    }

    /**
     * 팔로워들 목록 조회
     * */
    fun my_followers() = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = followRepository.get_my_follower(accessToken)
        response.enqueue(object : Callback<FollowListDto> {
            override fun onResponse(call: Call<FollowListDto>, response: Response<FollowListDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _followers.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<FollowListDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**
     * 팔로잉 목록 조회
     * */
    fun my_followings() = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = followRepository.get_my_followings(accessToken)
        response.enqueue(object : Callback<FollowListDto> {
            override fun onResponse(call: Call<FollowListDto>, response: Response<FollowListDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _followings.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<FollowListDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    //팔로우 하기
    fun follow(toUserId: Int) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = followRepository.follow(accessToken, toUserId)

        response.enqueue(object : Callback<FollowStateDto> {
            override fun onResponse(call: Call<FollowStateDto>, response: Response<FollowStateDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _isFollow.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<FollowStateDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    //팔로우 하기
    fun scrap(documentId : Int?=null, reportId : Int?=null, toktokId : Int?=null) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = followRepository.scrap(accessToken, LikeDto(documentId, toktokId, reportId, null) )

        response.enqueue(object : Callback<ScrapStateDto> {
            override fun onResponse(call: Call<ScrapStateDto>, response: Response<ScrapStateDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _isScrap.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ScrapStateDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}