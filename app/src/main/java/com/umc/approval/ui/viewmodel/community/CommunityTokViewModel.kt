package com.umc.approval.ui.viewmodel.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.community.get.CommunityTok
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.data.repository.AccessTokenRepository
import com.umc.approval.data.repository.community.CommunityRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityTokViewModel() : ViewModel() {

    //커뮤니티 리포지토리
    private val repository = CommunityRepository()

    //엑세스 토큰 리포지토리
    private val accessTokenRepository = AccessTokenRepository()

    /**tok 목록 라이브 데이터*/
    private var _tok_list = MutableLiveData<CommunityTokDto>()
    val tok_list : LiveData<CommunityTokDto>
        get() = _tok_list

    /**엑세스 토큰 여부 판단 라이브데이터*/
    private var _accessToken = MutableLiveData<Boolean>()
    val accessToken : LiveData<Boolean>
        get() = _accessToken

    //선택 데이터
    private var _select = MutableLiveData<Int>()
    val select : LiveData<Int>
        get() = _select


//    //테스트 데이터
//    fun init_all_toks() = viewModelScope.launch {
//
//        val init_data = mutableListOf<CommunityTok>()
//
//        var openGraphDto = OpenGraphDto(
//            "https://www.naver.com/",
//            "네이버",
//            "네이버"
//        )
//
//        init_data.add(
//            CommunityTok(
//                0,"", "강사원", 0, 0, "결재톡톡 보고서", "텀블러", false,
//                5,false, mutableListOf("image1", "image2"), mutableListOf(openGraphDto, openGraphDto),
//                mutableListOf("기계", "환경"), 13, 12, 12, "")
//        )
//
//        init_data.add(
//            CommunityTok(
//                0,"", "강사원", 0, 0, "결재톡톡 보고서", "텀블러", false,
//                5,false, mutableListOf("image1", "image2"), mutableListOf(openGraphDto, openGraphDto),
//                mutableListOf("기계", "환경"), 13, 12, 12, "")
//        )
//
//        init_data.add(
//            CommunityTok(
//                0,"", "강사원", 0, 0, "결재톡톡 보고서", "텀블러", false,
//                5,false, mutableListOf("image1", "image2"), mutableListOf(openGraphDto, openGraphDto),
//                mutableListOf("기계", "환경"), 13, 12, 12, "")
//        )
//
//        init_data.add(
//            CommunityTok(
//                0,"", "강사원", 0, 0, "결재톡톡 보고서", "텀블러", false,
//                5,false, mutableListOf("image1", "image2"), mutableListOf(openGraphDto, openGraphDto),
//                mutableListOf("기계", "환경"), 13, 12, 12, "")
//        )
//
//        //서버로부터 받아온 데이터
//        val communityTokDto = CommunityTokDto(init_data)
//
//        //데이터 삽입
//        _tok_list.postValue(communityTokDto)
//    }


    /**
     * 모든 documents 목록을 반환받는 메소드
     * 정상 동작 Check 완료
     * */
    fun get_all_toks(sortBy: Int ?= null) = viewModelScope.launch {

        var sort = sortBy
        if (sort == 3) {
            sort = null
        }

        val response = repository.get_toks(sort)
        response.enqueue(object : Callback<CommunityTokDto> {
            override fun onResponse(call: Call<CommunityTokDto>, response: Response<CommunityTokDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _tok_list.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<CommunityTokDto>, t: Throwable) {
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