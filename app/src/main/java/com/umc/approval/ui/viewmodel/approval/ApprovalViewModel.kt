package com.umc.approval.ui.viewmodel.approval

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import kotlinx.coroutines.launch

class ApprovalViewModel() : ViewModel() {

    private var _approval_all_list = MutableLiveData<ApprovalPaperDto>()
    val approval_all_list : LiveData<ApprovalPaperDto>
        get() = _approval_all_list

    private var _approval_interest_list = MutableLiveData<ApprovalPaperDto>()
    val approval_interest_list : LiveData<ApprovalPaperDto>
        get() = _approval_interest_list

    /**
     * init approval list
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
     * init approval list
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
}