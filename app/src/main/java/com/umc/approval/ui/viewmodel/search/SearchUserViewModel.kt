package com.umc.approval.ui.viewmodel.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.search.get.SearchUserDto
import com.umc.approval.data.repository.search.SearchFragmentApiRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchUserViewModel  : ViewModel() {
    // 검색 결과 리포지토리
    private val repository = SearchFragmentApiRepository()

    /** 결재보고서 목록 데이터*/
    private var _report = MutableLiveData<SearchUserDto?>()
    val report : LiveData<SearchUserDto?>
        get() = _report

    /** 검색어 */
    private var _query = MutableLiveData<String?>()
    val query : LiveData<String?>
        get() = _query


    fun setQuery(string: String?) {
        _query.postValue(string)
    }


    fun get_user(query:String?=null) = viewModelScope.launch {
        var query = query?:_query.value ?: ""

        val response = repository.search_user(query)
        response.enqueue(object : Callback<SearchUserDto> {
            override fun onResponse(call: Call<SearchUserDto>, response: Response<SearchUserDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _report.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<SearchUserDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}