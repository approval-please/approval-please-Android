package com.umc.approval.ui.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchKeywordViewModel: ViewModel() {
    /** 검색어 변수 */
    private var _search_keyword = MutableLiveData<String>()
    val search_keyword : LiveData<String>
        get() = _search_keyword

    fun setSearchKeyword(query: String) {
        _search_keyword.value = query
    }
}