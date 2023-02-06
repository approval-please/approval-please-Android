package com.umc.approval.data.dto.search.get

import com.google.gson.annotations.SerializedName

data class SearchUserInfoDto (
    @SerializedName("userId")
    var userId : Int,
    @SerializedName("nickname")
    var nickname : String,
    @SerializedName("profileImage")
    var profileImage : String ? = null,
    @SerializedName("level")
    var level : Int,
)