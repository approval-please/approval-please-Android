package com.umc.approval.data.dto.communitydetail.get

import com.google.gson.annotations.SerializedName

data class PostUserDto (
    @SerializedName("nickName")
    val nickname : String,
    @SerializedName("level")
    val level : String,
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("isFollow")
    val isFollow : Boolean,
    @SerializedName("mine")
    val mine : Boolean
)