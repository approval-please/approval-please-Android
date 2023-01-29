package com.umc.approval.data.dto.mypage

import com.google.gson.annotations.SerializedName
import java.util.*

data class Record(
    @SerializedName("content") // 내용
    val content : String,
    @SerializedName("point") // 포인트
    val point : Int,
    @SerializedName("datetime") // 날짜
    val datetime : String
)