package com.umc.approval.data.dto.communitydetail.get

import com.google.gson.annotations.SerializedName

data class VoteParticipationDto (
    @SerializedName("content")
    val content: List<VoteUserDto>?,
)

data class VoteUserDto(
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("level")
    val level: Int,
    @SerializedName("followOrNot")
    val followOrNot: Boolean,
)