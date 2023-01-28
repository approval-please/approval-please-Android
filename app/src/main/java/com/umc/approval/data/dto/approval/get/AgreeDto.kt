package com.umc.approval.data.dto.approval.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**
 * 동의 및 거절 수 API
 * API 명세서 Check 완료
 * */
data class AgreeDto(

    @SerializedName("approveCount")
    val approveCount: Int,
    @SerializedName("rejectCount")
    val rejectCount: Int,

)