package com.umc.approval.data.dto.community.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.util.VoteItem

data class CommunityVoteInfo(
    @SerializedName("writer")
    var writer: String,
    @SerializedName("voteTitle")
    var voteTitle: String,
    @SerializedName("voteState")
    var voteState: String,
    @SerializedName("voteOption")
    var voteOption: String?,
    @SerializedName("voteParticipants")
    var voteParticipants: Int,
    @SerializedName("voteItems")
    var voteItem: MutableList<VoteItem>? = mutableListOf<VoteItem>(),
)