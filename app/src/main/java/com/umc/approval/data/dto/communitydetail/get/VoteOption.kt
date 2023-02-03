package com.umc.approval.data.dto.communitydetail.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

data class VoteOption(
    @SerializedName("voteOptionId")
    val voteOptionId: Int,
    @SerializedName("opt")
    val opt: String,
)
