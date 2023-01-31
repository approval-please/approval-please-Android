package com.umc.approval.data.dto.communityReport.get

import com.google.gson.annotations.SerializedName

data class CommunityReportLink(
    @SerializedName("url")
    var url : String,
    @SerializedName("title")
    var title : String,
    @SerializedName("image")
    var image : String
)
