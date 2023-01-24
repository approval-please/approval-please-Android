package com.umc.approval.ui.viewmodel.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.mypage.Profile
import kotlinx.coroutines.launch

/**My Page ViewModel*/
class MypageViewModel() : ViewModel() {

    private var _myInfo = MutableLiveData<Profile>()
    val myInfo : LiveData<Profile>
        get() = _myInfo

    /**testdata*/
    fun init_test_data() = viewModelScope.launch {
        val profile = Profile(profileImage = null, 3, "팀", 36, 24, 36, introduction = null)
        _myInfo.postValue(profile)
    }
}