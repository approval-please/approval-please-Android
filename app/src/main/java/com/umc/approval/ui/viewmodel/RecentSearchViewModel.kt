package com.umc.approval.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RecentSearchViewModel() : ViewModel() {

    private var _search_text = MutableLiveData<List<String>>()
    val search_text : LiveData<List<String>>
        get() = _search_text

    fun searchKeyword(query: String) = viewModelScope.launch {
        val initList = mutableListOf<String>()
        if (query != "") {
            initList.add(query)
            initList.add(query)
            initList.add(query)
            initList.add(query)
            initList.add(query)
            initList.add(query)
        }
        _search_text.postValue(initList)
    }
}