package com.umc.approval.ui.viewmodel.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.data.repository.search.SearchFragmentApiRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchTokViewModel() : ViewModel() {
    //검색 결과 리포지토리
    private val repository = SearchFragmentApiRepository()

    /** 결재톡톡 목록 데이터*/
    private var _toktok = MutableLiveData<CommunityTokDto>()
    val toktok : LiveData<CommunityTokDto>
        get() = _toktok

    /** 검색어 */
    private var _query = MutableLiveData<String>()
    val query : LiveData<String>
        get() = _query

    /** 태그 여부 */
    private var _tag = MutableLiveData<Int>()
    val tag : LiveData<Int>
        get () = _tag

    /**카테고리 선택 라이브 데이터*/
    private var _category = MutableLiveData<Int>()
    val category : LiveData<Int>
        get() = _category

    /** 정렬 상태 라이브 데이터 */
    private var _sort = MutableLiveData<Int>()
    val sort: LiveData<Int>
        get() = _sort

    fun setQuery(string: String) {
        _query.value = string
    }

    fun setTag(string: String) {
        if (string.startsWith('#'))
            _tag.value = 1
        else
            _tag.value = 0
    }

    fun setCategory(int: Int) {
        _category.postValue(int)
    }

    fun setSort(int: Int) {
        _sort.value = int
    }

    fun get_toktok(query : String, isTag: Int, category : Int ?= null, sortBy : Int) = viewModelScope.launch {
        var ct = category

        if (ct == 18) {
            ct = null
        }

        val response = repository.search_community_tok(query, isTag, ct, sortBy)
        response.enqueue(object : Callback<CommunityTokDto> {
            override fun onResponse(call: Call<CommunityTokDto>, response: Response<CommunityTokDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _toktok.postValue(response.body())
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