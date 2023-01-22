package com.umc.approval.data.dto.profile

import com.google.gson.annotations.SerializedName

/**
 * profile get or change dto
 * */
data class ProfileDto (
        @SerializedName("nickname")
        var nickname : String,
        @SerializedName("introduce")
        var introduce : String,
        @SerializedName("image")
        var image : String,
)