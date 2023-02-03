package com.umc.approval.ui.viewmodel.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.community.get.CommunityReport
import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.data.dto.opengraph.OpenGraphDto
import com.umc.approval.data.repository.AccessTokenRepository
import com.umc.approval.data.repository.community.CommunityRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 로직 체크 완료
 * */
class CommunityReportViewModel() : ViewModel() {

    private val repository = CommunityRepository()

    private var _report_list = MutableLiveData<CommunityReportDto>()
    val report_list : LiveData<CommunityReportDto>
        get() = _report_list

    /**
     * 모든 report 목록을 반환받는 메소드
     * 정상 동작 Check 완료
     * */
    fun get_all_reports(sortBy: Int ?= null) = viewModelScope.launch {

        var sort = sortBy
        if (sort == 3) {
            sort = null
        }

        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.get_reports(accessToken, sort)
        response.enqueue(object : Callback<CommunityReportDto> {
            override fun onResponse(call: Call<CommunityReportDto>, response: Response<CommunityReportDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _report_list.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<CommunityReportDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}