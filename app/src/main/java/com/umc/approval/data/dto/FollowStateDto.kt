package com.umc.approval.data.dto

import com.google.gson.annotations.SerializedName

data class FollowStateDto (
    @SerializedName("isFollow")
    val isFollow: Boolean
)