package com.umc.approval.ui.viewmodel.otherpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.profile.ProfileContentDto
import com.umc.approval.data.dto.profile.ProfileDto
import com.umc.approval.data.repository.otherpage.OtherPageActivityRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OtherpageViewModel() : ViewModel() {
    private val repository = OtherPageActivityRepository()

    /* 다른 사람 프로필 라이브 데이터*/
    private var _othersInfo = MutableLiveData<ProfileContentDto>()
    val othersInfo : LiveData<ProfileContentDto>
    get() = _othersInfo

    /* 다른 사람 프로필 조회 */
    fun other_profile(userId : Int) = viewModelScope.launch {
        val accessToken = AccessTokenDataStore().getAccessToken().first()
        val response = repository.get_other_page(accessToken, userId)
        response.enqueue(object : Callback<ProfileContentDto>{
            override fun onResponse(call: Call<ProfileContentDto>, response: Response<ProfileContentDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _othersInfo.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ProfileContentDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}