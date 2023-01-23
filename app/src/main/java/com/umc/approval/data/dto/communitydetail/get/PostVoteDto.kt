package com.umc.approval.data.dto.communitydetail.get

import com.google.gson.annotations.SerializedName

data class PostVoteDto(
    @SerializedName("content")
    val content: String,
    @SerializedName("participation")
    val participation: ArrayList<PostUserDto>,
)