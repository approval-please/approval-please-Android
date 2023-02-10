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
import com.umc.approval.data.dto.mypage.MyScrapDto
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.data.repository.approval.ApprovalFragmentRepository
import com.umc.approval.data.repository.mypage.MyPageFragmentRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/* 내 정보 스크랩 탭 데이터 ViewModel */
class MyPageScrapViewModel() : ViewModel() {
    private val repository = MyPageFragmentRepository()

    /* 댓글 탭 라이브 데이터 */
    private var _scrap = MutableLiveData<MyScrapDto>()
    val scrap : LiveData<MyScrapDto>
        get() = _scrap

    /* 스크랩 목록 조회 */
    fun get_my_scraps(postType : Int?, state: Int?) = viewModelScope.launch {
        val accessToken = AccessTokenDataStore().getAccessToken().first()
        val response = repository.get_my_scraps(accessToken, postType, state)
        response.enqueue(object : Callback<MyScrapDto> {
            override fun onResponse(call: Call<MyScrapDto>, response: Response<MyScrapDto>) {
                if(response.isSuccessful){
                    _scrap.postValue(response.body())
                    Log.d("RESPONSE", response.body().toString())
                }
                else{
                    Log.d("RESPONSE", "FAIL")
                }
            }

            override fun onFailure(call: Call<MyScrapDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }

    /* 스크랩 테스트 데이터 */
    fun init_my_scraps() = viewModelScope.launch {
        val approval_testList : MutableList<ApprovalPaper> = arrayListOf()
        approval_testList.apply {
            add(
                ApprovalPaper(0,0, "", "", mutableListOf("기계", "환경 "),
                    link = null, "https://approval-please.s3.ap-northeast-2.amazonaws.com/profile/test", 0,0,32,32, "50분전",
                    1000)
            )
        }
        val tok_testList : MutableList<CommunityTok> = arrayListOf()
        tok_testList.apply{
            add(
                CommunityTok(0, "", 0,0,0,"","", arrayListOf(""), OpenGraphDto(), listOf(""),0,0,0,"",
                    CommunityTokListVoteDto("",true,0,true,true,0)
                )
            )
        }

        val report_testList : MutableList<CommunityReport> = arrayListOf()
        report_testList.apply {
            add(
                CommunityReport(0, 0, "", 0, "", arrayListOf(""),
                    OpenGraphDto(),listOf(""),0,0,"",0,
                    CommunityReportDocumentDto(0,"","","",listOf(""),0)
                )
            )
        }
        val approvalPageDto = ApprovalPaperDto(approval_testList.count(), approval_testList)
        val communityTokDto = CommunityTokDto(tok_testList.count(), tok_testList)
        val communityReportDto = CommunityReportDto(report_testList.count(), report_testList)
        val dto = MyScrapDto(approvalPageDto.documentCount, approvalPageDto, communityTokDto.toktokCount, communityTokDto, communityReportDto.reportCount,communityReportDto)
        _scrap.postValue(dto)
    }
}