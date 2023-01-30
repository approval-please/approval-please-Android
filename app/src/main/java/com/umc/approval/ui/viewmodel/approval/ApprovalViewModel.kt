package com.umc.approval.ui.viewmodel.approval

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
class ApprovalViewModel() : ViewModel() {

    //결재 서류 리포지토리
    private val repository = ApprovalFragmentRepository()

    //엑세스 토큰 리포지토리
    private val accessTokenRepository = AccessTokenRepository()

    /**전체부서 목록 라이브 데이터*/
    private var _approval_all_list = MutableLiveData<ApprovalPaperDto>()
    val approval_all_list : LiveData<ApprovalPaperDto>
        get() = _approval_all_list

    /**관심부서 목록 라이브데이터*/
    private var _approval_interest_list = MutableLiveData<ApprovalPaperDto>()
    val approval_interest_list : LiveData<ApprovalPaperDto>
        get() = _approval_interest_list

    /**엑세스 토큰 여부 판단 라이브데이터*/
    private var _accessToken = MutableLiveData<Boolean>()
    val accessToken : LiveData<Boolean>
        get() = _accessToken

    /**관심부서 카테고리 리스트*/
    private var _interesting = MutableLiveData<List<Int>>()
    val interesting : LiveData<List<Int>>
        get() = _interesting

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
     * 모든 documents 목록을 반환받는 메소드
     * 정상 동작 Check 완료
     * */
    fun get_all_documents(category: String?=null, state: String?= null, sortBy: String?= null) = viewModelScope.launch {

        val response = repository.getDocuments(category, state, sortBy)
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

    /**
     * 모든 documents 목록을 반환받는 메소드
     * 정상 동작 Check 완료
     * */
    fun get_interesting_documents(category: String?= null, state: String?= null, sortBy: String?= null) = viewModelScope.launch {

        //엑세스 토큰이 없거나 유효하지 않으면 로그인 페이지로 이동
        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.getInterestingCategoryDocuments(accessToken, category, state, sortBy)
        response.enqueue(object : Callback<ApprovalPaperDto> {
            override fun onResponse(call: Call<ApprovalPaperDto>, response: Response<ApprovalPaperDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _approval_interest_list.postValue(response.body())
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
     * 관심부서 목록 API
     * 정상 동작 Check 완료
     * */
    fun get_interest() = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.getCategory(accessToken)
        response.enqueue(object : Callback<InterestingDto> {
            override fun onResponse(call: Call<InterestingDto>, response: Response<InterestingDto>) {
                if (response.isSuccessful) {
                    if (response.body()!!.likedCategory != null) {
                        _interesting.postValue(response.body()!!.likedCategory)
                    }
                    Log.d("RESPONSE", response.body().toString())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<InterestingDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**엑세스 체크 API
     * 정상 동작 Check 완료
     * */
    fun checkAccessToken() = viewModelScope.launch {
        val tokenValue = AccessTokenDataStore().getAccessToken().first()
        val response = accessTokenRepository.checkAccessToken(tokenValue)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", "Success")
                    _accessToken.postValue(true)
                } else {
                    Log.d("RESPONSE", "FAIL")
                    _accessToken.postValue(false)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}