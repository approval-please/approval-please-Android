package com.umc.approval.ui.viewmodel.follow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.common.CommonUserDto
import com.umc.approval.data.dto.mypage.FollowListDto
import com.umc.approval.data.repository.mypage.MyPageFragmentRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel() : ViewModel() {

    private val repository = MyPageFragmentRepository()

    private var _followers = MutableLiveData<FollowListDto>()
    val followers : LiveData<FollowListDto>
        get() = _followers

    private var _followings = MutableLiveData<FollowListDto>()
    val followings : LiveData<FollowListDto>
        get() = _followings

    /**init followings*/
    fun init_followers() {

//        /**
//         * 서버와 연결 로직
//         * 서버에서 follower 데이터 받아오기
//         *
//         *
//         *
//         *
//         *
//         * */
//
//        var list = mutableListOf<CommonUserDto>()
//        list.add(CommonUserDto("", "지사원", 0, true))
//        list.add(CommonUserDto("", "김차장", 0, true))
//        list.add(CommonUserDto("", "최부장", 0, true))
//        list.add(CommonUserDto("", "유부장", 0, true))
//        list.add(CommonUserDto("", "김인턴", 0, true))
//        list.add(CommonUserDto("", "이부장", 0, true))
//        list.add(CommonUserDto("", "지차장", 0, true))
//        list.add(CommonUserDto("", "안사원", 0, true))
//        list.add(CommonUserDto("", "이부장", 0, true))
//        list.add(CommonUserDto("", "안상무", 0, true))
//        list.add(CommonUserDto("", "안사원", 0, true))
//
//        _followers.postValue(list)
    }

    /**init followers*/
    fun init_followings() {

//        /**
//         * 서버와 연결 로직
//         * 서버에서 following 데이터 받아오기
//         *
//         *
//         *
//         *
//         *
//         * */
//
//        var list = mutableListOf<CommonUserDto>()
//        list.add(CommonUserDto("", "김부장", 0, true))
//        list.add(CommonUserDto("", "김차장", 0, true))
//        list.add(CommonUserDto("", "지차장", 0, true))
//        list.add(CommonUserDto("", "김차장", 0, true))
//        list.add(CommonUserDto("", "김부장", 0, true))
//        list.add(CommonUserDto("", "이부장", 0, true))
//        list.add(CommonUserDto("", "지차장", 0, true))
//        list.add(CommonUserDto("", "안사원", 0, true))
//        list.add(CommonUserDto("", "이부장", 0, true))
//        list.add(CommonUserDto("", "안상무", 0, true))
//        list.add(CommonUserDto("", "안사원", 0, true))
//
//        _followings.postValue(list)
    }

    /**
     * 팔로워들 목록 조회
     * */
    fun my_followers() = viewModelScope.launch {

        val response = repository.get_my_follower("abc")
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

        val response = repository.get_my_followings("abc")
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
}