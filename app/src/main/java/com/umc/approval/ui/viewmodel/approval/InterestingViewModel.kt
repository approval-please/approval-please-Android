package com.umc.approval.ui.viewmodel.approval

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.InterestingDto
import com.umc.approval.data.dto.approval.post.InterestingPostDto
import com.umc.approval.data.repository.approval.ApprovalFragmentRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 전체 부서와 관심부서 정보를 서버에서 받아와 적용하는 로직 필요
 * ViewModel 체크 완료
 * */
class InterestingViewModel() : ViewModel() {

    //결재 서류 리포지토리
    private val repository = ApprovalFragmentRepository()

    /**관심부서 카테고리 리스트*/
    private var _interesting = MutableLiveData<List<Int>>()
    val interesting : LiveData<List<Int>>
        get() = _interesting

    /**관심부서 카테고리 리스트*/
    private var _not_interesting = MutableLiveData<List<Int>>()
    val not_interesting : LiveData<List<Int>>
        get() = _not_interesting

    /**전체부서 카테고리 리스트*/
    private var _all = MutableLiveData<List<Int>>()
    val all : LiveData<List<Int>>
        get() = _all

    fun setAll(all : List<Int>) {
        _all.postValue(all)
    }

    fun setNot(all : List<Int>) {
        _not_interesting.postValue(all)
    }

    /**
     * 관심부서 태그 목록 API
     * 정상 동작 Check 완료
     * */
    fun get_interest() = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.getCategory(accessToken)
        response.enqueue(object : Callback<InterestingDto> {
            override fun onResponse(call: Call<InterestingDto>, response: Response<InterestingDto>) {
                if (response.isSuccessful) {
                    if (response.body()!!.likedCategory != null) {
                        _interesting.postValue(response.body()!!.likedCategory)
                    } else {
                        _not_interesting.postValue(all.value)
                    }
                    Log.d("RESPONSE", response.body().toString())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<InterestingDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**
     * 관심부서 태그 목록 추가 API
     * 정상 동작 Check 완료
     * */
    fun post_interest(int: Int, boolean: Boolean) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()
        val list = mutableListOf<Int>()

        //true면 추가 false는 삭제
        if (boolean == true) {
            if (interesting.value != null) {
                for (i in interesting.value!!) {
                    list.add(i)
                }
            }
            list.add(int)
        } else {
            if (interesting.value != null) {
                for (i in interesting.value!!) {
                    if (i != int) {
                        list.add(i)
                    }
                }
            }
        }

        val response = repository.setCategory(accessToken, InterestingPostDto(list))
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    get_interest()
                    Log.d("RESPONSE", response.body().toString())
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