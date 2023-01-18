package com.umc.approval.ui.viewmodel.upload

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UploadViewModel() : ViewModel() {

    private var _pic = MutableLiveData<List<Uri>>()
    val pic : LiveData<List<Uri>>
        get() = _pic

    fun setImage(images: List<Uri>) {
        _pic.value = images
    }
}