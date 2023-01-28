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
import com.umc.approval.data.repository.community.CommunityRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityTokViewModel() : ViewModel() {

    private val repository = CommunityRepository()

    private var _tok_list = MutableLiveData<CommunityTokDto>()
    val tok_list : LiveData<CommunityTokDto>
        get() = _tok_list

    /**
     * init tok list
     * */
    fun init_all_toks() = viewModelScope.launch {

        val init_data = mutableListOf<CommunityTok>()

        var openGraphDto = OpenGraphDto(
            "https://www.naver.com/",
            "네이버",
            "네이버",
            "네이버",
            "https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png"
        )

        init_data.add(
            CommunityTok(
                0,"", "강사원", 0, 0, "결재톡톡 보고서", "텀블러", false,
                5,false, mutableListOf("image1", "image2"), mutableListOf(openGraphDto, openGraphDto),
                mutableListOf("기계", "환경"), 13, 12, 12, "")
        )

        init_data.add(
            CommunityTok(
                0,"", "강사원", 0, 0, "결재톡톡 보고서", "텀블러", false,
                5,false, mutableListOf("image1", "image2"), mutableListOf(openGraphDto, openGraphDto),
                mutableListOf("기계", "환경"), 13, 12, 12, "")
        )

        init_data.add(
            CommunityTok(
                0,"", "강사원", 0, 0, "결재톡톡 보고서", "텀블러", false,
                5,false, mutableListOf("image1", "image2"), mutableListOf(openGraphDto, openGraphDto),
                mutableListOf("기계", "환경"), 13, 12, 12, "")
        )

        init_data.add(
            CommunityTok(
                0,"", "강사원", 0, 0, "결재톡톡 보고서", "텀블러", false,
                5,false, mutableListOf("image1", "image2"), mutableListOf(openGraphDto, openGraphDto),
                mutableListOf("기계", "환경"), 13, 12, 12, "")
        )

        //서버로부터 받아온 데이터
        val communityTokDto = CommunityTokDto(init_data)

        //데이터 삽입
        _tok_list.postValue(communityTokDto)
    }


    /**
     * 모든 documents 목록을 반환받는 메소드
     * 정상 동작 Check 완료
     * */
    fun get_all_toks(sortBy: Int) = viewModelScope.launch {

        val response = repository.get_toks(sortBy)
        response.enqueue(object : Callback<CommunityTokDto> {
            override fun onResponse(call: Call<CommunityTokDto>, response: Response<CommunityTokDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    //나중에 서버와 연결시 활성화
                    //_approval_all_list.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<CommunityTokDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}