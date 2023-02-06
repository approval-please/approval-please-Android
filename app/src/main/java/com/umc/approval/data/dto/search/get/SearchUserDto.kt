package com.umc.approval.data.dto.search.get

import com.google.gson.annotations.SerializedName

data class SearchUserDto(
    @SerializedName("userCount")
    var userCount : Int,
    @SerializedName("content")
    var content : List<SearchUserInfoDto>?=null,
)
