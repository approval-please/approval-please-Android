package com.umc.approval.ui.viewmodel.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umc.approval.data.dto.profile.ProfileChange

class ProfileChangeViewModel() : ViewModel() {

    /**load profile change*/
    private var _load_profile = MutableLiveData<ProfileChange>()
    val load_profile : LiveData<ProfileChange>
        get() = _load_profile

    /**post profile change*/
    private var _post_profile = MutableLiveData<ProfileChange>()
    val post_profile : LiveData<ProfileChange>
        get() = _post_profile


    /**image*/
    private var _image = MutableLiveData<Uri>()
    val image : LiveData<Uri>
        get() = _image

    /**init data*/
    fun init_data() {
        //서버에서 가지고 오는 데이터
        val profileChange = ProfileChange("팀", "저는 안드로이드 파트 개발을 진행중입니다")
        _load_profile.postValue(profileChange)
    }

    /**set image data*/
    fun setImage(image: Uri) {
        _image.postValue(image)
    }

    /**set image data*/
    fun save(profile : ProfileChange) {
    }
}