package com.umc.approval.data.dto.communitydetail.post

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.communitydetail.get.VoteOption

data class CommunityVotePost (
    @SerializedName("voteOptionIds")
    var voteOptionIds : List<Int>
)

