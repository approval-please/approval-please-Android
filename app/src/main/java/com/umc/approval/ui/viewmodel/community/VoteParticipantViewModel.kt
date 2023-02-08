package com.umc.approval.ui.viewmodel.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.approval.data.dto.community.get.VoteParticipantDto
import com.umc.approval.data.repository.community.VoteParticipantRepository
import com.umc.approval.dataStore.AccessTokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VoteParticipantViewModel :ViewModel() {

    /**approval repository*/
    private val repository = VoteParticipantRepository()

    // 좋아요 누른 유저 목록
    private var _participantList = MutableLiveData<VoteParticipantDto>()
    val participantList: LiveData<VoteParticipantDto>
        get() = _participantList

    /**
     * 참가자 불러오기 API
     * 정상 동작 Check 완료
     * */
    fun get_community_tok_vote_participant(voteOptionId: Int) = viewModelScope.launch {
        val accessToken = AccessTokenDataStore().getAccessToken().first()

        val response = repository.get_community_tok_vote_participant(accessToken, voteOptionId)
        response.enqueue(object : Callback<VoteParticipantDto> {
            override fun onResponse(call: Call<VoteParticipantDto>, response: Response<VoteParticipantDto>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE", response.body().toString())
                    _participantList.postValue(response.body())
                } else {
                    Log.d("RESPONSE", "FAIL")
                }
            }
            override fun onFailure(call: Call<VoteParticipantDto>, t: Throwable) {
                Log.d("ContinueFail", "FAIL")
            }
        })
    }
}