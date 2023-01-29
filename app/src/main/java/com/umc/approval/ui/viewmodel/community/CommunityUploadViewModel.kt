package com.umc.approval.ui.viewmodel.community

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.data.dto.upload.post.ApprovalUploadDto
import com.umc.approval.data.dto.upload.post.TalkUploadDto
import com.umc.approval.data.repository.approval.ApprovalFragmentRepository
import com.umc.approval.data.repository.community.CommunityRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityUploadViewModel() : ViewModel() {

    private val repository = CommunityRepository()

    /**link Livedata*/
    private var _tabId = MutableLiveData<Int>()
    val tabId : LiveData<Int>
        get() = _tabId

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

    /**Set tabId*/
    fun setLink(tab: Int) {
        _tabId.postValue(tab)
    }

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

    /**
     * 서류를 등록하는 메서드
     * 정상 동작 Check 완료
     * */
    fun post_tok(tok: TalkUploadDto) = viewModelScope.launch {

        //엑세스 토큰이 없거나 유효하지 않으면 로그인 페이지로 이동
        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.tok_upload(accessToken, tok)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    //나중에 서버와 연결시 활성화
                    //_approval_all_list.postValue(response.body())
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