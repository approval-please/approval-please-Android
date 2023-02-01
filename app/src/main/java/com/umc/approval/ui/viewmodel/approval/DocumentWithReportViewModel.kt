package com.umc.approval.ui.viewmodel.approval

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.*
import com.umc.approval.data.dto.approval.post.AgreeMyPostDto
import com.umc.approval.data.dto.approval.post.AgreePostDto
import com.umc.approval.data.dto.approval.post.LikeDto
import com.umc.approval.data.dto.approval.post.TokLikeDto
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.data.repository.AccessTokenRepository
import com.umc.approval.data.repository.approval.ApprovalFragmentRepository
import com.umc.approval.data.repository.like.LikeRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Document 상세 보기 페이지
 * ViewModel 체크 완료
 * */
class DocumentWithReportViewModel() : ViewModel() {

    /**approval repository*/
    private val repository = ApprovalFragmentRepository()

    //서버에서 받아올 서류 데이터
    private var _documents = MutableLiveData<DocumentWithReportContentDto>()
    val documents : LiveData<DocumentWithReportContentDto>
        get() = _documents

    /**
     * 모든 리포트와 연결 가능한 documents 목록을 반환받는 메소드
     * 정상 동작 Check 완료
     * */
    fun get_documents() = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.getDocumentsWithReports(accessToken)
        response.enqueue(object : Callback<DocumentWithReportContentDto> {
            override fun onResponse(call: Call<DocumentWithReportContentDto>, response: Response<DocumentWithReportContentDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _documents.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<DocumentWithReportContentDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}