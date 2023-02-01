package com.umc.approval.data.dto.approval.post

import com.google.gson.annotations.SerializedName

/**
 * 관심부서 API
 * API 명세서 Check 완료
 * */
data class InterestingPostDto(

    @SerializedName("likedCategory")
    val likedCategory: List<Int>,
)