package com.umc.approval.data.dto.opengraph

import com.google.gson.annotations.SerializedName

/**open graph dto*/
data class OpenGraphDto(

    @SerializedName("url")
    var url: String ? = "",
    @SerializedName("siteName")
    var siteName: String ? = "",
    @SerializedName("title")
    var title: String ? = "",
    @SerializedName("description")
    var description: String ? = "",
    @SerializedName("image")
    var image: String ? = ""
)
