package com.umc.approval.data.dto.mypage

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query
import java.util.*

/**
실적 탭 구성
*/
data class Record(
    @SerializedName("date") // 날짜
    val date : Date,
    @SerializedName("content") // 내용
    val content : String,
    @SerializedName("point") // 포인트
    val point : Int
)