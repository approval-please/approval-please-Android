package com.umc.approval.ui.viewmodel.otherpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.repository.otherpage.OtherPageActivityRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtherpageDocumentViewModel() : ViewModel() {
    private val repository = OtherPageActivityRepository()

    private val _document = MutableLiveData<ApprovalPaperDto>()
    val document : LiveData<ApprovalPaperDto>
    get() = _document

    fun get_other_document(userId : Int, state : Int? = null) = viewModelScope.launch {
        val response = repository.get_other_document(userId, state)
        response.enqueue(object : Callback<ApprovalPaperDto>{
            override fun onResponse(
                call: Call<ApprovalPaperDto>,
                response: Response<ApprovalPaperDto>
            ) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _document.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ApprovalPaperDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    fun init_other_document() = viewModelScope.launch {
        val approvalPaperList : MutableList<ApprovalPaper> = arrayListOf()
        approvalPaperList.apply {
            add(
                ApprovalPaper(0,0, "", "", mutableListOf("기계", "환경 "),
                    link = null, "https://approval-please.s3.ap-northeast-2.amazonaws.com/profile/test", 0,0,32,32, "50분전",
                    1000)
            )
            add(
                ApprovalPaper(0,0, "", "", mutableListOf("기계", "환경 "),
                    link = null, "https://approval-please.s3.ap-northeast-2.amazonaws.com/profile/test", 0,0,32,32, "50분전",
                    1000)
            )
            add(
                ApprovalPaper(0,0, "", "", mutableListOf("기계", "환경 "),
                    link = null, "https://approval-please.s3.ap-northeast-2.amazonaws.com/profile/test", 0,0,32,32, "50분전",
                    1000)
            )
        }
        val test = ApprovalPaperDto(approvalPaperList.count(), approvalPaperList)
        _document.postValue(test)
    }
}