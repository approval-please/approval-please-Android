package com.umc.approval.data.dto.communitydetail.post

import com.google.gson.annotations.SerializedName

data class CommunityVoteResult (
    @SerializedName("votePeopleEachOption")
    var votePeopleEachOption : List<Int>
)