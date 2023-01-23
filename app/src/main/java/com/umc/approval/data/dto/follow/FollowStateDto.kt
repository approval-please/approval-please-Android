package com.umc.approval.data.dto.follow

import com.google.gson.annotations.SerializedName

data class FollowStateDto (
    @SerializedName("isFollow")
    val isFollow: Boolean
)