package com.umc.approval.ui.viewmodel.approval

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.AgreeDto
import com.umc.approval.data.dto.approval.get.DocumentDto
import com.umc.approval.data.dto.approval.get.InterestingDto
import com.umc.approval.data.dto.approval.get.LikeReturnDto
import com.umc.approval.data.dto.approval.post.AgreeMyPostDto
import com.umc.approval.data.dto.approval.post.AgreePostDto
import com.umc.approval.data.dto.approval.post.LikeDto
import com.umc.approval.data.dto.approval.post.TokLikeDto
import com.umc.approval.data.dto.opengraph.OpenGraphDto
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
 * Document 상세 보기 페이지
 * 서버로부터 데이터를 받아와 뷰에 적용하는 로직 필요
 * ViewModel 체크 완료
 * */
class DocumentViewModel() : ViewModel() {

    /**approval repository*/
    private val repository = ApprovalFragmentRepository()

    //엑세스 토큰 체크 리포지토리
    private val accessTokenRepository = AccessTokenRepository()

    //서버에서 받아올 서류 데이터
    private var _document = MutableLiveData<DocumentDto>()
    val document : LiveData<DocumentDto>
        get() = _document

    //엑세스 토큰이 존재할시 True값 지정, True 시 로그인 상태
    private var _accessToken = MutableLiveData<Boolean>()
    val accessToken : LiveData<Boolean>
        get() = _accessToken

    //카테고리 목록을 가지고옴
    private var _category = MutableLiveData<InterestingDto>()
    val category : LiveData<InterestingDto>
        get() = _category

    /**
     * 모든 documents 목록을 반환받는 메소드
     * 정상 동작 Check 완료
     * */
    fun get_document_detail(documentId : String) = viewModelScope.launch {

        val response = repository.getDocumentDetail(documentId)
        response.enqueue(object : Callback<DocumentDto> {
            override fun onResponse(call: Call<DocumentDto>, response: Response<DocumentDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _document.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<DocumentDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**
     * document를 삭제하는 메서드
     * 정상 동작 Check 완료, 단 연결 필요
     * */
    fun delete_document(documentId : String) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.deleteDocument(accessToken, documentId)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**
     * 타 게시물 승인 및 반려 API
     * 정상 동작 Check 완료, 단 연결 필요
     * */
    fun agree_document(documentId : String, agreePostDto: AgreePostDto) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.agreeDocument(accessToken, documentId, agreePostDto)
        response.enqueue(object : Callback<AgreeDto> {
            override fun onResponse(call: Call<AgreeDto>, response: Response<AgreeDto>) {
                get_document_detail(documentId)
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<AgreeDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /**
     * 본인 게시물 승인 및 반려 API
     * 정상 동작 Check 완료, 단 연결 필요
     * */
    fun agree_my_document(agreeMyPostDto: AgreeMyPostDto) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.agreeMyDocument(accessToken, agreeMyPostDto)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }


    fun document_like() = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.like(accessToken, LikeDto(documentId = 1))
        response.enqueue(object : Callback<LikeReturnDto> {
            override fun onResponse(call: Call<LikeReturnDto>, response: Response<LikeReturnDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<LikeReturnDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }


    /**엑세스 토큰 체크*/
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