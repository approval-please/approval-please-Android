package com.umc.approval.data.dto.mypage

import com.google.gson.annotations.SerializedName

data class RecordDto(
    @SerializedName("totalElement")
    val totalElement: Int,
    @SerializedName("content")
    val content: List<Record>
)