package com.umc.approval.ui.viewmodel.approval

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.approval.get.DocumentDto
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.data.dto.upload.post.ApprovalUploadDto
import com.umc.approval.data.repository.AccessTokenRepository
import com.umc.approval.data.repository.approval.ApprovalFragmentRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Document 업로드 페이지
 * 변경 데이터를 서버로 업로드하는 로직 필요
 * ViewModel 체크 완료
 * */
class UploadDocumentViewModel() : ViewModel() {

    //결재 서류 리포지토리
    private val repository = ApprovalFragmentRepository()

    //엑세스 토큰 리포지토리
    private val accessTokenRepository = AccessTokenRepository()

    //엑세스 토큰 여부 판단 라이브데이터
    private var _accessToken = MutableLiveData<Boolean>()
    val accessToken : LiveData<Boolean>
        get() = _accessToken

    //카테고리 데이터
    private var _category = MutableLiveData<Int>()
    val category : LiveData<Int>
        get() = _category

    /**이미지 Livedata*/
    private var _pic = MutableLiveData<List<Uri>>()
    val pic : LiveData<List<Uri>>
        get() = _pic

    /**images Livedata*/
    private var _images = MutableLiveData<List<String>>()
    val images : LiveData<List<String>>
        get() = _images

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

    /**image 선택시 적용*/
    fun setImage(images: List<Uri>) {
        _pic.postValue(images)
    }

    /**link가 올바른 경우 Opengraph 적용*/
    fun setOpengraph(og: OpenGraphDto) {
        _opengraph.postValue(og)
    }

    /**link 선택시 적용*/
    fun setLink(li: String) {
        _link.postValue(li)
    }

    /**tag 선택시 적용*/
    fun setTags(tags: List<String>) {
        _tags.postValue(tags)
    }

    /**images 선택시 적용*/
    fun setImages(tags: List<String>) {
        _images.postValue(tags)
    }

    /**tag 선택시 적용*/
    fun setCategory(cate: Int) {
        _category.postValue(cate)
    }


    /**
     * 서류를 등록하는 메서드
     * 정상 동작 Check 완료
     * */
    fun post_document(upload: ApprovalUploadDto) = viewModelScope.launch {

        //엑세스 토큰이 없거나 유효하지 않으면 로그인 페이지로 이동
        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.postDocument(accessToken, upload)
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

    /**엑세스 토큰 체크*/
    fun checkAccessToken() = viewModelScope.launch {
        val tokenValue = AccessTokenDataStore().getAccessToken().first()
        val response = accessTokenRepository.checkAccessToken(tokenValue)
        response.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", "Success")
                    _accessToken.postValue(true)
                } else {
                    Log.d("RESPONSE", "FAIL")
                    _accessToken.postValue(false)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}