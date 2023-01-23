package com.umc.approval.data.dto.mypage

import com.google.gson.annotations.SerializedName

data class RecordDto(
    @SerializedName("recordDto")
    val recordDto: List<Record>
)