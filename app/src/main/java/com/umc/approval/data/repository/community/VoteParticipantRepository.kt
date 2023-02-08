package com.umc.approval.data.repository.community

import com.umc.approval.data.dto.community.get.VoteParticipantDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance
import retrofit2.Call

class VoteParticipantRepository {

    /**get participant detail*/
    fun get_community_tok_vote_participant(accessToken: String, voteOptionId: Int): Call<VoteParticipantDto> {
        return RetrofitInstance.communityApi.get_community_tok_vote_participant(accessToken, voteOptionId)
    }
}