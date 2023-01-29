package com.umc.approval.ui.viewmodel.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.mypage.Profile
import com.umc.approval.data.dto.mypage.RecordDto
import com.umc.approval.data.dto.profile.ProfileDto
import com.umc.approval.data.repository.mypage.MyPageFragmentRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**My Page ViewModel*/
class MypageViewModel() : ViewModel() {

    private val repository = MyPageFragmentRepository()

    /**내 프로필 라이브 데이터*/
    private var _myInfo = MutableLiveData<ProfileDto>()
    val myInfo : LiveData<ProfileDto>
        get() = _myInfo

    /**실적 탭 라이브 데이터*/
    private var _performances = MutableLiveData<RecordDto>()
    val performances : LiveData<RecordDto>
        get() = _performances

    /**서류 탭 라이브 데이터*/
    private var _document = MutableLiveData<ApprovalPaperDto>()
    val document : LiveData<ApprovalPaperDto>
        get() = _document

    /**
     * mypage 프로필 조회
     * */
    fun my_profile() = viewModelScope.launch {

        val response = repository.get_my_page("abc")
        response.enqueue(object : Callback<ProfileDto> {
            override fun onResponse(call: Call<ProfileDto>, response: Response<ProfileDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _myInfo.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ProfileDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**
     * 마이페이지 내 결재 서류 목록 조회
     * */
    fun get_my_documents(state: Int?=null, isApproved : Int?=null) = viewModelScope.launch {

        val response = repository.get_my_documents("abc", state, isApproved)
        response.enqueue(object : Callback<ApprovalPaperDto> {
            override fun onResponse(call: Call<ApprovalPaperDto>, response: Response<ApprovalPaperDto>) {
                if (response.isSuccessful) {
                    _document.postValue(response.body())
                    Log.d("RESPONSE", response.body().toString())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ApprovalPaperDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**
     * 다른 사람 결재 서류 목록 조회
     * */
    fun get_other_documents(userId: Int, state: Int?=null, isApproved : Int?=null) = viewModelScope.launch {

        val response = repository.get_other_documents("abc", userId, state, isApproved)
        response.enqueue(object : Callback<ApprovalPaperDto> {
            override fun onResponse(call: Call<ApprovalPaperDto>, response: Response<ApprovalPaperDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ApprovalPaperDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**
     * 다른 사람 결재 서류 목록 조회
     * */
    fun get_my_performances() = viewModelScope.launch {

        val response = repository.get_my_perfoemances("abc")
        response.enqueue(object : Callback<RecordDto> {
            override fun onResponse(call: Call<RecordDto>, response: Response<RecordDto>) {
                if (response.isSuccessful) {
                    _performances.postValue(response.body())
                    Log.d("RESPONSE", response.body().toString())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<RecordDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}