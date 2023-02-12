package com.umc.approval.ui.viewmodel.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.profile.ProfileChange
import com.umc.approval.data.dto.profile.ProfileContentDto
import com.umc.approval.data.dto.profile.ProfileDto
import com.umc.approval.data.repository.mypage.MyPageFragmentRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileChangeViewModel() : ViewModel() {

    private val repository = MyPageFragmentRepository()

    /**load profile change*/
    private var _load_profile = MutableLiveData<ProfileDto>()
    val load_profile : LiveData<ProfileDto>
        get() = _load_profile

    /**image*/
    private var _image = MutableLiveData<Uri>()
    val image : LiveData<Uri>
        get() = _image

    /**set image data*/
    fun setImage(image: Uri) {
        _image.postValue(image)
    }

    /**
     * mypage 프로필 조회
     * */
    fun my_profile() = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()
        val response = repository.get_my_page(accessToken)

        response.enqueue(object : Callback<ProfileContentDto> {
            override fun onResponse(call: Call<ProfileContentDto>, response: Response<ProfileContentDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _load_profile.postValue(response.body()!!.content)
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ProfileContentDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
    /**
     * mypage 프로필 조회
     * */
    fun change_profile(profile: ProfileChange) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()
        if (profile.image == null) {
            profile.image = load_profile.value!!.profileImage
        }
        val response = repository.change_my_profile(accessToken, profile)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}