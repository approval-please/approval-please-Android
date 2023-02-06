package com.umc.approval.ui.viewmodel.follow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.LikeReturnDto
import com.umc.approval.data.dto.approval.post.LikeDto
import com.umc.approval.data.dto.common.CommonUserDto
import com.umc.approval.data.dto.communitydetail.post.CommunityVoteResult
import com.umc.approval.data.dto.follow.FollowStateDto
import com.umc.approval.data.dto.follow.NotificationStateDto
import com.umc.approval.data.dto.follow.ScrapStateDto
import com.umc.approval.data.dto.mypage.FollowListDto
import com.umc.approval.data.repository.follow.FollowFragmentRepository
import com.umc.approval.data.repository.like.LikeRepository
import com.umc.approval.data.repository.mypage.MyPageFragmentRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel() : ViewModel() {

    private val followRepository = FollowFragmentRepository()

    //라이크 리포지토리
    private val likeRepository = LikeRepository()

    private var _followers = MutableLiveData<FollowListDto>()
    val followers : LiveData<FollowListDto>
        get() = _followers

    private var _followings = MutableLiveData<FollowListDto>()
    val followings : LiveData<FollowListDto>
        get() = _followings

    //팔로우
    private var _isFollow = MutableLiveData<FollowStateDto>()
    val isFollow : LiveData<FollowStateDto>
        get() = _isFollow

    fun setFollow(li:FollowStateDto) {
        _isFollow.postValue(li)
    }

    //스크랩
    private var _isScrap = MutableLiveData<ScrapStateDto>()
    val isScrap : LiveData<ScrapStateDto>
        get() = _isScrap

    fun setScrap(li:ScrapStateDto) {
        _isScrap.postValue(li)
    }

    //알림설정
    private var _notif = MutableLiveData<NotificationStateDto>()
    val notif : LiveData<NotificationStateDto>
        get() = _notif

    fun setNotification(li:NotificationStateDto) {
        _notif.postValue(li)
    }

    //좋아요
    private var _like = MutableLiveData<Boolean>()
    val like : LiveData<Boolean>
        get() = _like

    fun setLike(li: Boolean) {
        _like.postValue(li)
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

    //스크랩 하기
    fun scrap(documentId : Int?=null, reportId : Int?=null, toktokId : Int?=null) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = followRepository.scrap(accessToken, LikeDto(documentId, toktokId, reportId) )

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

    //알림설정하기
    fun notification(documentId : Int?=null, reportId : Int?=null, toktokId : Int?=null) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = followRepository.notification(accessToken, LikeDto(documentId, toktokId, reportId) )

        response.enqueue(object : Callback<NotificationStateDto> {
            override fun onResponse(call: Call<NotificationStateDto>, response: Response<NotificationStateDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _notif.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<NotificationStateDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    //신고하기
    fun accuse(documentId : Int?=null, reportId : Int?=null,
               toktokId : Int?=null, commentId : Int?=null, accuseUserId : Int?=null) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = followRepository.notification(accessToken, LikeDto(documentId, toktokId, reportId, commentId, accuseUserId))

        response.enqueue(object : Callback<NotificationStateDto> {
            override fun onResponse(call: Call<NotificationStateDto>, response: Response<NotificationStateDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _notif.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<NotificationStateDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    fun like(documentId: Int?=null, toktokId: Int?=null, reportId: Int?=null) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = likeRepository.like(accessToken,
            LikeDto(documentId = documentId, toktokId = toktokId, reportId = reportId))

        response.enqueue(object : Callback<LikeReturnDto> {
            override fun onResponse(call: Call<LikeReturnDto>, response: Response<LikeReturnDto>) {
                if (response.isSuccessful) {
                    _like.postValue(response.body()!!.isLike)
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<LikeReturnDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}