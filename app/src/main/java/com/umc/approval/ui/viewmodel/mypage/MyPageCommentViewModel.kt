package com.umc.approval.ui.viewmodel.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.community.get.*
import com.umc.approval.data.dto.mypage.MyCommentDto
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.data.repository.mypage.MyPageFragmentRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageCommentViewModel() : ViewModel() {
    private val repository = MyPageFragmentRepository()

    /* 댓글 탭 라이브 데이터 */
    private var _comment = MutableLiveData<MyCommentDto>()
    val comment : LiveData<MyCommentDto>
        get() = _comment

    /* 댓글 목록 조회 */
    fun get_my_comments(postType : Int?, state: Int?) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.get_my_comments(accessToken, postType, state)
        response.enqueue(object : Callback<MyCommentDto> {
            override fun onResponse(call: Call<MyCommentDto>, response: Response<MyCommentDto>) {
                if(response.isSuccessful){
                    _comment.postValue(response.body())
                    Log.d("RESPONSE", response.body().toString())
                }
                else{
                    Log.d("RESPONSE", "FAIL")
                }
            }

            override fun onFailure(call: Call<MyCommentDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}