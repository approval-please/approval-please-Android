package com.umc.approval.ui.viewmodel.approval

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 정렬 선택 데이터 선택
 * ViewModel 체크 완료
 * */
class ApprovalCommonViewModel() : ViewModel() {

    /**승인 반려 순*/
    private var _state  = MutableLiveData<String>()
    val state  : LiveData<String>
        get() = _state

    /**최신 인기순 정렬*/
    private var _sortBy  = MutableLiveData<String>()
    val sortBy  : LiveData<String>
        get() = _sortBy

    fun setState(int: Int) {
        _state.postValue(int.toString())
    }

    fun setSort(int: Int) {
        _sortBy.postValue(int.toString())
    }
}