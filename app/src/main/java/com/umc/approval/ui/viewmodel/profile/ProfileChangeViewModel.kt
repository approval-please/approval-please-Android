package com.umc.approval.ui.viewmodel.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.profile.ProfileChange
import com.umc.approval.data.dto.profile.ProfileDto
import com.umc.approval.data.repository.mypage.MyPageFragmentRepository
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

        val response = repository.get_my_page("abc")
        response.enqueue(object : Callback<ProfileDto> {
            override fun onResponse(call: Call<ProfileDto>, response: Response<ProfileDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _load_profile.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<ProfileDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
    /**
     * mypage 프로필 조회
     * */
    fun change_profile(profile: ProfileChange) = viewModelScope.launch {

        val response = repository.change_my_profile("abc", profile)
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