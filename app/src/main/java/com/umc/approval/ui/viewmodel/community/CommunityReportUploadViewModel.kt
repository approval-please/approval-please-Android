package com.umc.approval.ui.viewmodel.community

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.data.dto.upload.post.ReportUploadDto
import com.umc.approval.data.dto.upload.post.TalkUploadDto
import com.umc.approval.data.repository.AccessTokenRepository
import com.umc.approval.data.repository.community.CommunityRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityReportUploadViewModel() : ViewModel() {

    //커뮤니티 리포지토리
    private val repository = CommunityRepository()

    /**
     * 리포트 업로드부분 라이브데이터
     * */
    //결재서류 아이디 라이브데이터
    private var _documentId = MutableLiveData<Int>()
    val documentId : LiveData<Int>
        get() = _documentId

    //내용 라이브 데이터
    private var _content = MutableLiveData<String>()
    val content : LiveData<String>
        get() = _content

    //이미지 url 라이브데이터
    private var _pic = MutableLiveData<List<Uri>>()
    val pic : LiveData<List<Uri>>
        get() = _pic

    //이미지 라이브데이터
    private var _images = MutableLiveData<List<String>>()
    val images : LiveData<List<String>>
        get() = _images

    //태그 라이브데이터
    private var _tags = MutableLiveData<List<String>>()
    val tags : LiveData<List<String>>
        get() = _tags

    //링크 라이브데이터
    private var _opengraph_list = MutableLiveData<List<OpenGraphDto>>()
    val opengraph_list : LiveData<List<OpenGraphDto>>
        get() = _opengraph_list

    //링크 자체 라이브데이터
    private var _link_list = MutableLiveData<List<String>>()
    val link_list : LiveData<List<String>>
        get() = _link_list

    //각 링크당 만들어지는 오픈 그래프 라이브 데이터
    private var _opengraph = MutableLiveData<OpenGraphDto>()
    val opengraph : LiveData<OpenGraphDto>
        get() = _opengraph

    //각 링크 입력시 라이브데이터
    private var _link = MutableLiveData<String>()
    val link : LiveData<String>
        get() = _link

    fun setTags(tags: List<String>) {
        _tags.postValue(tags)
    }

    /**Set images livedata*/
    fun setImage(images: List<Uri>) {
        _pic.postValue(images)
    }

    /**Set images livedata*/
    fun setRealImage(images: List<String>) {
        _images.postValue(images)
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
    fun setContent(li: String) {
        _content.postValue(li)
    }

    /**Set Opengraph list livedata*/
    fun setDocumentId(li: Int) {
        _documentId.postValue(li)
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
        }

        //마지막 데이터 삽입
        og_list.add(og)

        _opengraph_list.postValue(og_list)
    }

    /**
     * 리포트 업로드 API
     * 정상 동작 Check 완료
     * */
    fun post_report(reportUploadDto: ReportUploadDto) = viewModelScope.launch {

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.report_upload(accessToken, reportUploadDto)
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