package com.umc.approval.data.dto.profile

import com.google.gson.annotations.SerializedName

/**
 * profile get or change dto
 * */
data class ProfileChange (
        @SerializedName("nickname")
        var nickname : String ? = null,
        @SerializedName("introduction")
        var introduction : String ? = null,
        @SerializedName("image")
        var image : String ? = null,
)