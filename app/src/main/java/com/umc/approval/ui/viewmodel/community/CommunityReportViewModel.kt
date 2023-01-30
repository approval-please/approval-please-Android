package com.umc.approval.ui.viewmodel.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.community.get.CommunityReport
import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.data.repository.community.CommunityRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityReportViewModel() : ViewModel() {

    private val repository = CommunityRepository()

    private var _report_list = MutableLiveData<CommunityReportDto>()
    val report_list : LiveData<CommunityReportDto>
        get() = _report_list

    /**
     * init report list
     * */
    fun init_all_reports() = viewModelScope.launch {

        val init_data = mutableListOf<CommunityReport>()

        var openGraphDto = OpenGraphDto(
            "https://www.naver.com/",
            "네이버",
            "네이버"
        )

        init_data.add(
            CommunityReport(
                0, 0, "강사원", 0, "","", mutableListOf(""),
                mutableListOf(""),"", mutableListOf(""), mutableListOf(openGraphDto, openGraphDto),
                mutableListOf(""),0,false,false,0,0,"",0)
        )

        init_data.add(
            CommunityReport(
                0, 0, "강사원", 0, "","", mutableListOf(""),
                mutableListOf(""),"", mutableListOf(""), mutableListOf(openGraphDto, openGraphDto),
                mutableListOf(""),0,false,false,0,0,"",0)
        )

        init_data.add(
            CommunityReport(
                0, 0, "강사원", 0, "","", mutableListOf(""),
                mutableListOf(""),"", mutableListOf(""), mutableListOf(openGraphDto, openGraphDto),
                mutableListOf(""),0,false,false,0,0,"",0)
        )

        init_data.add(
            CommunityReport(
                0, 0, "강사원", 0, "","", mutableListOf(""),
                mutableListOf(""),"", mutableListOf(""), mutableListOf(openGraphDto, openGraphDto),
                mutableListOf(""),0,false,false,0,0,"",0)
        )

        //서버로부터 받아온 데이터
        val communityReportDto = CommunityReportDto(init_data)

        //데이터 삽입
        _report_list.postValue(communityReportDto)
    }

    /**
     * 모든 documents 목록을 반환받는 메소드
     * 정상 동작 Check 완료
     * */
    fun get_all_reports(sortBy: Int ?= null) = viewModelScope.launch {

        val response = repository.get_reports(sortBy)
        response.enqueue(object : Callback<CommunityReportDto> {
            override fun onResponse(call: Call<CommunityReportDto>, response: Response<CommunityReportDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _report_list.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<CommunityReportDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}