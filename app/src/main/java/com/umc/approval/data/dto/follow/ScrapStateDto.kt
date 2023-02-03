package com.umc.approval.data.dto.follow

import com.google.gson.annotations.SerializedName

data class ScrapStateDto (
    @SerializedName("isScrap")
    val isScrap: Boolean
)