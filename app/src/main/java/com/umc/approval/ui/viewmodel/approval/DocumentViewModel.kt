package com.umc.approval.ui.viewmodel.approval

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.DocumentDto
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import kotlinx.coroutines.launch

/**
 * Document 상세 보기 페이지
 * 서버로부터 데이터를 받아와 뷰에 적용하는 로직 필요
 * */
class DocumentViewModel() : ViewModel() {

    private var _document = MutableLiveData<DocumentDto>()
    val document : LiveData<DocumentDto>
        get() = _document

    /**
     * init approval list
     * */
    fun init_document() = viewModelScope.launch {

        var openGraphDto = OpenGraphDto(
            "https://www.naver.com/",
            "네이버",
            "네이버",
            "네이버",
            "https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png"
        )

        //서버로부터 받아온 데이터
        val documentDto = DocumentDto(2,1, "23/01/11 15:30",
            "aws", "팀", mutableListOf("aws", "aws"), "아이폰 14 pro", "아이폰 14 pro 살까요 말까요",
            openGraphDto, mutableListOf("태그", "태그"), 13, 2, 10, 10, 20, 20)

        //데이터 삽입
        _document.postValue(documentDto)
    }
}