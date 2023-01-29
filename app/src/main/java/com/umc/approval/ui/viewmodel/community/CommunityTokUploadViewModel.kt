package com.umc.approval.ui.viewmodel.community

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import kotlinx.coroutines.launch

class CommunityTokUploadViewModel() : ViewModel() {

    /**이미지 Livedata*/
    private var _pic = MutableLiveData<List<Uri>>()
    val pic : LiveData<List<Uri>>
        get() = _pic

    /**Open graph list Livedata*/
    private var _opengraph_list = MutableLiveData<List<OpenGraphDto>>()
    val opengraph_list : LiveData<List<OpenGraphDto>>
        get() = _opengraph_list

    /**link list Livedata*/
    private var _link_list = MutableLiveData<List<String>>()
    val link_list : LiveData<List<String>>
        get() = _link_list

    /**Open graph Livedata*/
    private var _opengraph = MutableLiveData<OpenGraphDto>()
    val opengraph : LiveData<OpenGraphDto>
        get() = _opengraph

    /**link Livedata*/
    private var _link = MutableLiveData<String>()
    val link : LiveData<String>
        get() = _link

    /**Set images livedata*/
    fun setImage(images: List<Uri>) {
        _pic.postValue(images)
    }

    /**Set Opengraph livedata*/
    fun setOpengraph(og: OpenGraphDto) {
        _opengraph.postValue(og)
    }

    /**Set Opengraph list livedata*/
    fun setLink(li: String) {
        _link.postValue(li)
    }

    /**Set Opengraph list livedata*/
    fun setOpengraph_list(og: OpenGraphDto) = viewModelScope.launch {

        val og_list = mutableListOf<OpenGraphDto>()

        //현재 리스트가 오픈그래프가 있고 안에 내용이 있는 경우
        if (_opengraph_list.value != null) {

            //오픈 그래프가 있는 경우
            if (!_opengraph_list.value!!.isEmpty()) {
                for (i in _opengraph_list.value!!) {
                    og_list.add(i)
                }
            }
            //마지막 데이터 삽입
            og_list.add(og)
        }

        _opengraph_list.postValue(og_list)
    }
}