package com.umc.approval.ui.viewmodel.approval

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.DocumentDto
import com.umc.approval.data.dto.comment.CommentListDto
import com.umc.approval.data.dto.comment.DocumentCommentDto
import com.umc.approval.data.dto.opengraph.OpenGraphDto
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

    private var _document = MutableLiveData<DocumentDto>()
    val document : LiveData<DocumentDto>
        get() = _document

    /**
     * init approval list
     * 데모데이를 위한 테스트 로직
     * */
    fun init_document() = viewModelScope.launch {

        var openGraphDto = OpenGraphDto(
            "https://www.naver.com/",
            "네이버",
            "네이버",
            "네이버",
            "https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png"
        )

        //서버로부터 받아온 데이터
        val documentDto = DocumentDto(1,2,1, "23/01/11 15:30",
            "aws", "팀", mutableListOf("aws", "aws"), "아이폰 14 pro", "아이폰 14 pro 살까요 말까요",
            openGraphDto, mutableListOf("태그", "태그"), 13, 2, 10, 10,
            20, 20, false)

        //데이터 삽입
        _document.postValue(documentDto)
    }

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
                    //나중에 서버와 연결시 활성화
                    //_approval_all_list.postValue(response.body())
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
     * 모든 comments 목록을 반환받는 메소드
     * 정상 동작 Check 완료
     * */
    fun get_comments() = viewModelScope.launch {

        val response = repository.getComments(1)
        response.enqueue(object : Callback<CommentListDto> {
            override fun onResponse(call: Call<CommentListDto>, response: Response<CommentListDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    //나중에 서버와 연결시 활성화
                    //_approval_all_list.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<CommentListDto>, t: Throwable) {
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
}