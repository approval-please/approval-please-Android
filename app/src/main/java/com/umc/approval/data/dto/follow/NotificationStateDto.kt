package com.umc.approval.data.dto.follow

import com.google.gson.annotations.SerializedName

data class NotificationStateDto (
    @SerializedName("isNotification")
    val isNotification: Boolean
)