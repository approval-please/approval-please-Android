package com.umc.approval.ui.viewmodel.comment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.AgreeDto
import com.umc.approval.data.dto.approval.get.DocumentDto
import com.umc.approval.data.dto.approval.post.AgreeMyPostDto
import com.umc.approval.data.dto.approval.post.AgreePostDto
import com.umc.approval.data.dto.comment.get.CommentListDto
import com.umc.approval.data.dto.comment.post.CommentPostDto
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.data.repository.approval.ApprovalFragmentRepository
import com.umc.approval.data.repository.comment.CommentRepository
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
class CommentViewModel() : ViewModel() {

    /**approval repository*/
    private val repository = CommentRepository()

    //서버에서 받아올 서류 데이터
    private var _comments = MutableLiveData<CommentListDto>()
    val comments : LiveData<CommentListDto>
        get() = _comments

    /**
     * 모든 comments 목록을 API
     * 정상 동작 Check 완료
     * */
    fun get_comments(documentId: String?=null, toktokId : String?=null, reportId : String?=null) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.getComments(accessToken, documentId, toktokId, reportId)
        response.enqueue(object : Callback<CommentListDto> {
            override fun onResponse(call: Call<CommentListDto>, response: Response<CommentListDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _comments.postValue(response.body())
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
     * 댓글 작성 API
     * 정상 동작 Check 완료
     * */
    fun post_comments(commentPostDto: CommentPostDto) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.postComments(accessToken, commentPostDto)
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
     * Comment 삭제 API
     * 정상 동작 Check 완료
     * */
    fun delete_comments(type: Int, commentId : String) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.deleteComment(accessToken, commentId)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    get_comments()
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