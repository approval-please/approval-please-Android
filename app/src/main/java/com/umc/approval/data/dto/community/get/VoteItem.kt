package com.umc.approval.data.dto.community.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.util.VoteItem

data class VoteItem(
    @SerializedName("id")
    var id: Int,
    @SerializedName("check")
    var check: Boolean,
    @SerializedName("content")
    var content: String,
    @SerializedName("participation")
    var participation: Int,
)