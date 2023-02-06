package com.umc.approval.ui.viewmodel.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.approval.get.InterestingDto
import com.umc.approval.data.repository.AccessTokenRepository
import com.umc.approval.data.repository.approval.ApprovalFragmentRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 전체 부서와 관심부서 정보를 서버에서 받아와 적용하는 로직 필요
 * ViewModel 체크 완료
 * */
class MyPageApprovalViewModel() : ViewModel() {

    //결재 서류 리포지토리
    private val repository = ApprovalFragmentRepository()

    /**전체부서 목록 라이브 데이터*/
    private var _approval_all_list = MutableLiveData<ApprovalPaperDto>()
    val approval_all_list : LiveData<ApprovalPaperDto>
        get() = _approval_all_list

    /**카테고리 선택 라이브 데이터*/
    private var _state = MutableLiveData<String>()
    val state : LiveData<String>
        get() = _state

    /**승인 반려 순*/
    private var _isApproved = MutableLiveData<String>()
    val isApproved  : LiveData<String>
        get() = _isApproved

    fun setState(int: Int) {
        _state.postValue(int.toString())
    }

    fun setApproved(int: Int) {
        _isApproved.postValue(int.toString())
    }

    /**
     * 테스트 데이터는 아래와 같이 작성하면 됨
     * 전체부서 가지고 오는 로직
     * */
    fun init_all_category_approval() = viewModelScope.launch {

        val approvalPaperList: MutableList<ApprovalPaper> = arrayListOf()

        approvalPaperList.apply{
            add(
                ApprovalPaper(0,0, "", "", mutableListOf("기계", "환경 "),
                    link = null, "https://approval-please.s3.ap-northeast-2.amazonaws.com/profile/test", 0,0,32,32, "50분전",
                    1000)
            )

            add(
                ApprovalPaper(1,2, "", "", mutableListOf("기계", "환경 "),
                    link = null, "https://approval-please.s3.ap-northeast-2.amazonaws.com/profile/test", 0,2,32,32, "50분전",
                    1000)
            )

            add(
                ApprovalPaper(2,1, "", "", mutableListOf("기계", "환경 "),
                    link = null, "https://approval-please.s3.ap-northeast-2.amazonaws.com/profile/test", 0,1,32,32, "50분전",
                    1000)
            )
        }

        //서버로부터 받아온 데이터
        val approvalPageDto = ApprovalPaperDto(0, approvalPaperList)

        //데이터 삽입
        _approval_all_list.postValue(approvalPageDto)
    }

    /**
     * 관심부서 서류 목록을 반환받는 API
     * 정상 동작 Check 완료
     * */
    fun get_mypage_documents(state: String?= null, isApproved: String?= null) = viewModelScope.launch {

        var sta = state
        var appro = isApproved

        if (sta == "3") { //3이 default이므로 null
            sta = null
        }

        if (appro == "2") { //2가 default이므로 null
            appro = null
        }

        val accessToken = AccessTokenDataStore().getAccessToken().first()
        val response = repository.getDocuments_MyPage(accessToken, sta, appro)
        response.enqueue(object : Callback<ApprovalPaperDto> {
            override fun onResponse(call: Call<ApprovalPaperDto>, response: Response<ApprovalPaperDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _approval_all_list.postValue(response.body())
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