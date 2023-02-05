package com.umc.approval.ui.viewmodel.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.repository.search.SearchFragmentApiRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchDocumentViewModel : ViewModel() {
    // 검색 결과 리포지토리
    private val repository = SearchFragmentApiRepository()

    /** 결재보고서 목록 데이터*/
    private var _report = MutableLiveData<ApprovalPaperDto?>()
    val report : LiveData<ApprovalPaperDto?>
        get() = _report

    /** 검색어 */
    private var _query = MutableLiveData<String?>()
    val query : LiveData<String?>
        get() = _query

    /**카테고리 선택 라이브 데이터*/
    private var _category = MutableLiveData<Int?>()
    val category : LiveData<Int?>
        get() = _category

    /**상태 선택 라이브 데이터*/
    private var _state = MutableLiveData<Int?>()
    val state : LiveData<Int?>
        get() = _state

    /** 정렬 상태 라이브 데이터 */
    private var _sort = MutableLiveData<Int>()
    val sort: LiveData<Int>
        get() = _sort

    fun setQuery(string: String?) {
        _query.postValue(string)
    }

    fun setCategory(int: Int?) {
        _category.postValue(int)
    }

    fun setSort(int: Int) {
        _sort.postValue(int)
    }

    fun setState(int : Int?){
        _state.postValue(int)
    }

    fun get_documents(query:String?=null) = viewModelScope.launch {
        var query = query?:_query.value ?: ""
        var isTag = 0
        if(query.contains('#')) isTag =1
        var category = _category.value
        var state = _state.value
        var sortBy = _sort.value?:0


        val response = repository.search_documents(query, isTag, category, state,sortBy)
        response.enqueue(object : Callback<ApprovalPaperDto> {
            override fun onResponse(call: Call<ApprovalPaperDto>, response: Response<ApprovalPaperDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _report.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ApprovalPaperDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}