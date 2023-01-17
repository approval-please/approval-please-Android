package com.umc.approval.ui.viewmodel.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileImageViewModel() : ViewModel() {

    private var _profile = MutableLiveData<Uri>()
    val profile : LiveData<Uri>
        get() = _profile

    fun setImage(images: Uri) {
        _profile.value = images
    }
}