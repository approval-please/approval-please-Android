package com.umc.approval.ui.viewmodel.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umc.approval.data.dto.mypage.MyPageDto

/**My Page ViewModel*/
class MypageViewModel() : ViewModel() {

    private var _myInfo = MutableLiveData<MyPageDto>()
    val myInfo : LiveData<MyPageDto>
        get() = _myInfo
}