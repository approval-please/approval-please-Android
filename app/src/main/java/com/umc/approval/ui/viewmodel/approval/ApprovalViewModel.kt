package com.umc.approval.ui.viewmodel.approval

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.repository.approval.ApprovalFragmentRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Approval Fragment 페이지
 * 전체 부서와 관심부서 정보를 서버에서 받아와 적용하는 로직 필요
 * ViewModel 체크 완료
 * */
class ApprovalViewModel() : ViewModel() {

    /**approval repository*/
    private val repository = ApprovalFragmentRepository()

    /**전체부서 라이브 데이터*/
    private var _approval_all_list = MutableLiveData<ApprovalPaperDto>()
    val approval_all_list : LiveData<ApprovalPaperDto>
        get() = _approval_all_list

    /**관심부서 라이브데이터*/
    private var _approval_interest_list = MutableLiveData<ApprovalPaperDto>()
    val approval_interest_list : LiveData<ApprovalPaperDto>
        get() = _approval_interest_list

    /**엑세스 토큰 여부 판단 라이브데이터*/
    private var _has_accessToken = MutableLiveData<Boolean>()
    val has_accessToken : LiveData<Boolean>
        get() = _has_accessToken

    /**
     * 데모데이 영상을 위한 로직
     * 전체부서 가지고 오는 로직
     * */
    fun init_all_category_approval() = viewModelScope.launch {

        val approvalPaperList: MutableList<ApprovalPaper> = arrayListOf()

        approvalPaperList.apply{
            add(
                ApprovalPaper(0,0, "", "", mutableListOf("기계", "환경 "),
                    "https://approval-please.s3.ap-northeast-2.amazonaws.com/profile/test", 0,0,32,32, "50분전",
                    1000)
            )

            add(
                ApprovalPaper(1,2, "", "", mutableListOf("기계", "환경 "),
                    "https://approval-please.s3.ap-northeast-2.amazonaws.com/profile/test", 0,2,32,32, "50분전",
                    1000)
            )

            add(
                ApprovalPaper(2,1, "", "", mutableListOf("기계", "환경 "),
                    "https://approval-please.s3.ap-northeast-2.amazonaws.com/profile/test", 0,1,32,32, "50분전",
                    1000)
            )
        }

        //서버로부터 받아온 데이터
        val approvalPageDto = ApprovalPaperDto(approvalPaperList)

        //데이터 삽입
        _approval_all_list.postValue(approvalPageDto)
    }


    /**
     * 데모데이 영상을 위한 로직
     * 관심부서 가지고오는 로직
     * */
    fun init_interest_category_approval() = viewModelScope.launch {

        val approvalPaperList: MutableList<ApprovalPaper> = arrayListOf()

        approvalPaperList.apply{
            add(
                ApprovalPaper(0,0, "", "", mutableListOf("기계", "환경 "),
                    "https://approval-please.s3.ap-northeast-2.amazonaws.com/profile/test", 0,0,32,32, "50분전",
                    1000)
            )

            add(
                ApprovalPaper(1,2, "", "", mutableListOf("기계", "환경 "),
                    "https://approval-please.s3.ap-northeast-2.amazonaws.com/profile/test", 0,2,32,32, "50분전",
                    1000)
            )

            add(
                ApprovalPaper(2,1, "", "", mutableListOf("기계", "환경 "),
                    "https://approval-please.s3.ap-northeast-2.amazonaws.com/profile/test", 0,1,32,32, "50분전",
                    1000)
            )
        }

        //서버로부터 받아온 데이터
        val approvalPageDto = ApprovalPaperDto(approvalPaperList)

        //데이터 삽입
        _approval_interest_list.postValue(approvalPageDto)
    }

    /**
     * 모든 documents 목록을 반환받는 메소드
     * 정상 동작 Check 완료
     * */
    fun get_all_documents(category: String?=null) = viewModelScope.launch {

        val response = repository.getDocuments(category)
        response.enqueue(object : Callback<ApprovalPaperDto> {
            override fun onResponse(call: Call<ApprovalPaperDto>, response: Response<ApprovalPaperDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    //나중에 서버와 연결시 활성화
                    //_approval_all_list.postValue(response.body())
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
     * 모든 documents 목록을 반환받는 메소드
     * 정상 동작 Check 완료
     * */
    fun get_interesting_documents(category: String?= null) = viewModelScope.launch {

        //엑세스 토큰이 없거나 유효하지 않으면 로그인 페이지로 이동
        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.getInterestingCategoryDocuments(accessToken, category)
        response.enqueue(object : Callback<ApprovalPaperDto> {
            override fun onResponse(call: Call<ApprovalPaperDto>, response: Response<ApprovalPaperDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    //나중에 서버와 연결시 활성화
                    //_approval_interest_list.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")

                    //엑세스 토큰이 없거나 만료된 경우
                    _has_accessToken.postValue(false)
                }
            }
            override fun onFailure(call: Call<ApprovalPaperDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}