package com.umc.approval.ui.viewmodel.upload

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umc.approval.data.dto.opengraph.OpenGraphDto

class CommunityReportUploadViewModel() : ViewModel() {

    /**이미지 Livedata*/
    private var _pic = MutableLiveData<List<Uri>>()
    val pic : LiveData<List<Uri>>
        get() = _pic

    fun setImage(images: List<Uri>) {
        _pic.postValue(images)
    }

    /**Open graph Livedata*/
    private var _opengraph = MutableLiveData<OpenGraphDto>()
    val opengraph : LiveData<OpenGraphDto>
        get() = _opengraph

    fun setOpengraph(og: OpenGraphDto) {
        _opengraph.postValue(og)
    }

    /**link Livedata*/
    private var _link = MutableLiveData<String>()
    val link : LiveData<String>
        get() = _link

    fun setLink(li: String) {
        _link.postValue(li)
    }
}