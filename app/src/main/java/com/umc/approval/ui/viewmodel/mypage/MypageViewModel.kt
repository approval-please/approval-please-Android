package com.umc.approval.ui.viewmodel.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.mypage.Profile
import com.umc.approval.data.dto.profile.ProfileDto
import com.umc.approval.data.repository.mypage.MyPageFragmentRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**My Page ViewModel*/
class MypageViewModel() : ViewModel() {

    private val repository = MyPageFragmentRepository()

    private var _myInfo = MutableLiveData<ProfileDto>()
    val myInfo : LiveData<ProfileDto>
        get() = _myInfo

    /**testdata*/
    fun init_test_data() = viewModelScope.launch {
//        val profile = Profile(profileImage = null, 3, "팀", 36, 24, 36, introduction = null)
//        _myInfo.postValue(profile)
    }

    /**
     * mypage 프로필 조회
     * */
    fun my_profile() = viewModelScope.launch {

        val response = repository.get_my_page("abc")
        response.enqueue(object : Callback<ProfileDto> {
            override fun onResponse(call: Call<ProfileDto>, response: Response<ProfileDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _myInfo.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ProfileDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}