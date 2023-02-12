package com.umc.approval.ui.viewmodel.otherpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.community.get.*
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.data.dto.otherpage.OtherCommunityDto
import com.umc.approval.data.repository.otherpage.OtherPageActivityRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtherpageCommunityViewModel() : ViewModel() {
    private val repository = OtherPageActivityRepository()

    private val _community = MutableLiveData<OtherCommunityDto>()
    val community : LiveData<OtherCommunityDto>
    get() = _community

    fun get_other_community(userId : Int, postType : Int? = null) = viewModelScope.launch {
        val response = repository.get_other_community(userId, postType)
        response.enqueue(object : Callback<OtherCommunityDto>{
            override fun onResponse(
                call: Call<OtherCommunityDto>,
                response: Response<OtherCommunityDto>
            ) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _community.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }

            override fun onFailure(call: Call<OtherCommunityDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}