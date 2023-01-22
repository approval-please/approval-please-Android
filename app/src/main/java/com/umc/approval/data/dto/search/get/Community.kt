package com.umc.approval.data.dto.search.get

import com.google.gson.annotations.SerializedName


data class Community(
    @SerializedName("report")
    val report: Report,
    @SerializedName("toktok")
    val toktok: Toktok
)