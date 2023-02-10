package com.umc.approval.ui.viewmodel.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.mypage.CommunityDto
import com.umc.approval.data.dto.mypage.Profile
import com.umc.approval.data.dto.mypage.RecordDto
import com.umc.approval.data.dto.profile.ProfileContentDto
import com.umc.approval.data.dto.profile.ProfileDto
import com.umc.approval.data.repository.AccessTokenRepository
import com.umc.approval.data.repository.mypage.MyPageFragmentRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**My Page ViewModel*/
class MypageViewModel() : ViewModel() {

    //엑세스 토큰 리포지토리
    private val accessTokenRepository = AccessTokenRepository()

    /**엑세스 토큰 여부 판단 라이브데이터*/
    private var _accessToken = MutableLiveData<Boolean>()
    val accessToken : LiveData<Boolean>
        get() = _accessToken

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

    /**커뮤니티 탭 라이브 데이터*/
    private var _community = MutableLiveData<CommunityDto>()
    val community : LiveData<CommunityDto>
        get() = _community

    /**
     * mypage 프로필 조회
     * */
    fun my_profile() = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()
        val response = repository.get_my_page(accessToken)
        response.enqueue(object : Callback<ProfileContentDto> {
            override fun onResponse(call: Call<ProfileContentDto>, response: Response<ProfileContentDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _myInfo.postValue(response.body()!!.content)
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ProfileContentDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /* otherpageDocumentViewModel로 이동
    /*
     * 다른 사람 결재 서류 목록 조회
     */
     fun get_other_documents(userId: Int, state: Int?=null, isApproved : Int?=null) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()
        val response = repository.get_other_documents(accessToken, userId, state, isApproved)
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
     */

    /**
     * 다른 사람 결재 서류 목록 조회
     * */
    fun get_my_performances() = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()
        val response = repository.get_my_perfoemances(accessToken)
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

    /**
     * 로그인 상태 체크 API
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