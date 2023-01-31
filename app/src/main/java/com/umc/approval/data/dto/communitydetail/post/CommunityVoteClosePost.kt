package com.umc.approval.data.dto.communitydetail.post

import com.google.gson.annotations.SerializedName

data class CommunityVoteClosePost(
    @SerializedName("voteOptionIds")
    var voteOptionIds : List<Int>
    )
