package com.umc.approval.ui.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.search.KeywordDto
import com.umc.approval.data.repository.search.RecentSearchFragmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Recent Search keyword ViewModel
 * */
class RecentSearchViewModel(private val repository: RecentSearchFragmentRepository) : ViewModel() {

    /**최근 검색어 변수*/
    private var _recent_keyword = MutableLiveData<List<KeywordDto>>()
    val recent_keyword : LiveData<List<KeywordDto>>
        get() = _recent_keyword

    /**최근 검색어 가지고 오는 메소드*/
    fun searchKeyword() = viewModelScope.launch {
        _recent_keyword.postValue(repository.getAllKeywords())
    }

    /**최근 검색어 추가하는 메소드*/
    fun addKeyword(keywordDto: KeywordDto) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertData(keywordDto)
    }

    /**최근 검색어 하나를 삭제해주는 메소드*/
    fun deleteKeyword(keywordDto: KeywordDto) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteOne(keywordDto)
        searchKeyword()
    }

    /**모든 최근 검색어를 삭제해주는 메소드*/
    fun deleteAllKeyword() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
        searchKeyword()
    }

    /**연관 검색어 변수*/
    private var _related_keyword = MutableLiveData<List<KeywordDto>>()
    val related_keyword : LiveData<List<KeywordDto>>
        get() = _related_keyword

    fun relatedKeyword(query: String) = viewModelScope.launch {
        _related_keyword.postValue(repository.getAllRelatedKeyword(query+"%"))
    }
}