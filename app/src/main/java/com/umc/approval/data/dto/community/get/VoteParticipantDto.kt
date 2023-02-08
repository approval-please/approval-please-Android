package com.umc.approval.data.dto.community.get

import com.google.gson.annotations.SerializedName

data class VoteParticipantDto(
    @SerializedName("content")
    var content: List<ParticipantDto>?=null,
)
