package com.umc.approval.data.dto.approval.get

import com.google.gson.annotations.SerializedName

/**
 * 관심부서 API
 * API 명세서 Check 완료
 * */
data class InterestingDto(

    @SerializedName("isSet")
    val isSet: Boolean,
    @SerializedName("likedCategory")
    val likedCategory: List<Int>,
)