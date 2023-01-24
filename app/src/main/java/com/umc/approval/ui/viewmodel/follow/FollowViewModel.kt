package com.umc.approval.ui.viewmodel.follow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.common.CommonUserDto
import kotlinx.coroutines.launch

class FollowViewModel() : ViewModel() {

    private var _followers = MutableLiveData<MutableList<CommonUserDto>>()
    val followers : LiveData<MutableList<CommonUserDto>>
        get() = _followers

    private var _followings = MutableLiveData<MutableList<CommonUserDto>>()
    val followings : LiveData<MutableList<CommonUserDto>>
        get() = _followings

    /**init followings*/
    fun init_followers() {

        /**
         * 서버와 연결 로직
         * 서버에서 follower 데이터 받아오기
         *
         *
         *
         *
         *
         * */

        var list = mutableListOf<CommonUserDto>()
        list.add(CommonUserDto("", "김부장", 0, true))
        list.add(CommonUserDto("", "김차장", 0, true))
        list.add(CommonUserDto("", "지차장", 0, true))
        list.add(CommonUserDto("", "김차장", 0, true))
        list.add(CommonUserDto("", "김부장", 0, true))
        list.add(CommonUserDto("", "이부장", 0, true))
        list.add(CommonUserDto("", "지차장", 0, true))
        list.add(CommonUserDto("", "안사원", 0, true))
        list.add(CommonUserDto("", "이부장", 0, true))
        list.add(CommonUserDto("", "안상무", 0, true))
        list.add(CommonUserDto("", "안사원", 0, true))

        _followers.postValue(list)
    }

    /**init followers*/
    fun init_followings() {

        /**
         * 서버와 연결 로직
         * 서버에서 following 데이터 받아오기
         *
         *
         *
         *
         *
         * */

        var list = mutableListOf<CommonUserDto>()
        list.add(CommonUserDto("", "김부장", 0, true))
        list.add(CommonUserDto("", "김차장", 0, true))
        list.add(CommonUserDto("", "지차장", 0, true))
        list.add(CommonUserDto("", "김차장", 0, true))
        list.add(CommonUserDto("", "김부장", 0, true))
        list.add(CommonUserDto("", "이부장", 0, true))
        list.add(CommonUserDto("", "지차장", 0, true))
        list.add(CommonUserDto("", "안사원", 0, true))
        list.add(CommonUserDto("", "이부장", 0, true))
        list.add(CommonUserDto("", "안상무", 0, true))
        list.add(CommonUserDto("", "안사원", 0, true))

        _followings.postValue(list)
    }

    /**post data*/
    fun get_followers(string: String) = viewModelScope.launch {

        Log.d("send to server", string)

        /**
         * 서버와 연결 로직
         * 서버에서 검색어에 따라 following Data 받아오기
         *
         *
         *
         * */

        var list = mutableListOf<CommonUserDto>()
        _followers.postValue(list)
    }

    /**post data*/
    fun get_followings(string: String) = viewModelScope.launch {

        Log.d("send to server", string)

        /**
         * 서버와 연결 로직
         * 서버에서 검색어에 따라 following Data 받아오기
         *
         *
         *
         *
         * */

        var list = mutableListOf<CommonUserDto>()
        _followers.postValue(list)
    }
}