package com.umc.approval.ui.viewmodel.upload

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.data.dto.upload.post.ApprovalUploadDto
import kotlinx.coroutines.launch
import java.util.function.LongFunction

class UploadViewModel() : ViewModel() {

    /**이미지 Livedata*/
    private var _pic = MutableLiveData<List<Uri>>()
    val pic : LiveData<List<Uri>>
        get() = _pic

    /**Open graph Livedata*/
    private var _opengraph = MutableLiveData<OpenGraphDto>()
    val opengraph : LiveData<OpenGraphDto>
        get() = _opengraph

    /**link Livedata*/
    private var _link = MutableLiveData<String>()
    val link : LiveData<String>
        get() = _link

    /**link Livedata*/
    private var _tags = MutableLiveData<List<String>>()
    val tags : LiveData<List<String>>
        get() = _tags

    fun setImage(images: List<Uri>) {
        _pic.postValue(images)
    }

    fun setOpengraph(og: OpenGraphDto) {
        _opengraph.postValue(og)
    }

    fun setLink(li: String) {
        _link.postValue(li)
    }

    fun setTags(tags: List<String>) {
        _tags.postValue(tags)
    }

    /**
     * upload
     * */
    fun upload(uploadDto: ApprovalUploadDto) = viewModelScope.launch {

        /**
         * 서버로 보낼 로직
         *
         * */

    }
}