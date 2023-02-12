package com.umc.approval.data.dto.community.get

import com.google.gson.annotations.SerializedName

data class ParticipantDto (
    @SerializedName("profileImage")
    var profileImage: String?=null,
    @SerializedName("nickname")
    var nickname: String,
    @SerializedName("level")
    var level: Int,
    @SerializedName("userId")
    var userId: Int,
    @SerializedName("isMy")
    var isMy: Boolean,
    @SerializedName("followOrNot")
    var followOrNot: Boolean,
)