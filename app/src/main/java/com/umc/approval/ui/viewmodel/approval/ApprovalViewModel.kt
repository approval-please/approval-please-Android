package com.umc.approval.ui.viewmodel.approval

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.repository.approval.ApprovalFragmentRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Approval Fragment 페이지
 * 전체 부서와 관심부서 정보를 서버에서 받아와 적용하는 로직 필요
 * */
class ApprovalViewModel() : ViewModel() {

    /**approval repository*/
    private val repository = ApprovalFragmentRepository()

    /**전체부서 라이브 데이터*/
    private var _approval_all_list = MutableLiveData<ApprovalPaperDto>()
    val approval_all_list : LiveData<ApprovalPaperDto>
        get() = _approval_all_list

    /**관심부서 라이브데이터*/
    private var _approval_interest_list = MutableLiveData<ApprovalPaperDto>()
    val approval_interest_list : LiveData<ApprovalPaperDto>
        get() = _approval_interest_list

    /**
     * 전체부서 가지고 오는 로직
     * */
    fun init_all_category_approval() = viewModelScope.launch {

        val approvalPaperList: MutableList<ApprovalPaper> = arrayListOf()

        approvalPaperList.apply{
            add(
                ApprovalPaper(0, 0, "30분전",
                mutableListOf(),
                "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                1000, 32, 12)
            )

            add(
                ApprovalPaper(1, 0, "30분전",
                mutableListOf("https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png"),
                "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                1000, 32, 12)
            )

            add(
                ApprovalPaper(2, 0, "30분전",
                mutableListOf(),
                "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                1000, 32, 12)
            )

            add(
                ApprovalPaper(0, 0, "30분전",
                mutableListOf("https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png"),
                "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                1000, 32, 12)
            )

            add(
                ApprovalPaper(1, 0, "30분전",
                mutableListOf("https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png"),
                "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                1000, 32, 12)
            )
        }

        //서버로부터 받아온 데이터
        val approvalPageDto = ApprovalPaperDto(0,0,0,approvalPaperList)

        //데이터 삽입
        _approval_all_list.postValue(approvalPageDto)
    }


    /**
     * 관심부서 가지고오는 로직
     * */
    fun init_interest_category_approval() = viewModelScope.launch {

        val approvalPaperList: MutableList<ApprovalPaper> = arrayListOf()

        approvalPaperList.apply{
            add(
                ApprovalPaper(0, 0, "30분전",
                    mutableListOf(),
                    "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                    1000, 32, 12)
            )

            add(
                ApprovalPaper(1, 0, "30분전",
                    mutableListOf("https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png"),
                    "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                    1000, 32, 12)
            )

            add(
                ApprovalPaper(2, 0, "30분전",
                    mutableListOf(),
                    "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                    1000, 32, 12)
            )

            add(
                ApprovalPaper(0, 0, "30분전",
                    mutableListOf("https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png"),
                    "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                    1000, 32, 12)
            )

            add(
                ApprovalPaper(1, 0, "30분전",
                    mutableListOf("https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png"),
                    "아이폰 14 Pro", "새로 출시된 아이폰 골드입니다", mutableListOf("기계", "환경 "),
                    1000, 32, 12)
            )
        }

        //서버로부터 받아온 데이터
        val approvalPageDto = ApprovalPaperDto(0,0,0,approvalPaperList)

        //데이터 삽입
        _approval_interest_list.postValue(approvalPageDto)
    }

    /**
     * 테스트
     * */
    fun test() = viewModelScope.launch {

        var approvalPaper = ApprovalPaper(0,0,
            "today", null, "텀블러", "살까요", listOf("가정", "환경"), 0, 0, 0)

        val approvalPaperDto = ApprovalPaperDto(0,0,0, listOf(approvalPaper))

        val response = repository.test(approvalPaperDto)
        response.enqueue(object : Callback<ApprovalPaperDto> {
            override fun onResponse(call: Call<ApprovalPaperDto>, response: Response<ApprovalPaperDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ApprovalPaperDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}