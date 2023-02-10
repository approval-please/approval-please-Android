package com.umc.approval.data.dto.communitydetail.post

import com.google.gson.annotations.SerializedName

data class CommunityVoteResult (
    //수정 필요
    @SerializedName("votePeopleEachOption")
    var votePeopleEachOption : List<Int>
)