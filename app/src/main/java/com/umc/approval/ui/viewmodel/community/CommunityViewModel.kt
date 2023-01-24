package com.umc.approval.ui.viewmodel.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.community.get.CommunityReport
import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.community.get.CommunityTok
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import kotlinx.coroutines.launch

class CommunityViewModel() : ViewModel() {

    private var _tok_list = MutableLiveData<CommunityTokDto>()
    val tok_list : LiveData<CommunityTokDto>
        get() = _tok_list

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
            "네이버",
            "네이버",
            "https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png"
        )

        init_data.add(
            CommunityReport(
                "강사원", 0, 0, 0, 0,
                "스타벅스 텀블러", "집에 텀블러 다섯개 있는데",
                mutableListOf("기계", "환경"), "잘 구매한 것 같아요",
                mutableListOf("image1", "image2"), mutableListOf(openGraphDto, openGraphDto),
                mutableListOf("기계", "환경"), 10)
        )

        init_data.add(
            CommunityReport(
                "강사원", 0, 0, 0, 0,
                "스타벅스 텀블러", "집에 텀블러 다섯개 있는데",
                mutableListOf("기계", "환경"), "잘 구매한 것 같아요",
                mutableListOf("image1", "image2"), mutableListOf(openGraphDto, openGraphDto),
                mutableListOf("기계", "환경"), 10)
        )

        init_data.add(
            CommunityReport(
                "강사원", 0, 0, 0, 0,
                "스타벅스 텀블러", "집에 텀블러 다섯개 있는데",
                mutableListOf("기계", "환경"), "잘 구매한 것 같아요",
                mutableListOf("image1", "image2"), mutableListOf(openGraphDto, openGraphDto),
                mutableListOf("기계", "환경"), 10)
        )

        init_data.add(
            CommunityReport(
                "강사원", 0, 0, 0, 0,
                "스타벅스 텀블러", "집에 텀블러 다섯개 있는데",
                mutableListOf("기계", "환경"), "잘 구매한 것 같아요",
                mutableListOf("image1", "image2"), mutableListOf(openGraphDto, openGraphDto),
                mutableListOf("기계", "환경"), 10)
        )

        //서버로부터 받아온 데이터
        val communityReportDto = CommunityReportDto(init_data)

        //데이터 삽입
        _report_list.postValue(communityReportDto)
    }


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
                "강사원", 0, "결재톡톡 보고서", "텀블러", 0,
                false, 0,
                mutableListOf("image1", "image2"), mutableListOf(openGraphDto, openGraphDto),
                mutableListOf("기계", "환경"), 13)
        )

        init_data.add(
            CommunityTok(
                "강사원", 0, "결재톡톡 보고서", "텀블러", 0,
                false, 0,
                mutableListOf("image1", "image2"), mutableListOf(openGraphDto, openGraphDto),
                mutableListOf("기계", "환경"), 13)
        )

        init_data.add(
            CommunityTok(
                "강사원", 0, "결재톡톡 보고서", "텀블러", 0,
                false, 0,
                mutableListOf("image1", "image2"), mutableListOf(openGraphDto, openGraphDto),
                mutableListOf("기계", "환경"), 13)
        )

        init_data.add(
            CommunityTok(
                "강사원", 0, "결재톡톡 보고서", "텀블러", 0,
                false, 0,
                mutableListOf("image1", "image2"), mutableListOf(openGraphDto, openGraphDto),
                mutableListOf("기계", "환경"), 13)
        )

        init_data.add(
            CommunityTok(
                "강사원", 0, "결재톡톡 보고서", "텀블러", 0,
                false, 0,
                mutableListOf("image1", "image2"), mutableListOf(openGraphDto, openGraphDto),
                mutableListOf("기계", "환경"), 13)
        )

        //서버로부터 받아온 데이터
        val communityTokDto = CommunityTokDto(init_data)

        //데이터 삽입
        _tok_list.postValue(communityTokDto)
    }
}