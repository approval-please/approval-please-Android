package com.umc.approval.data.dto.communityReport.get

import com.google.gson.annotations.SerializedName

/* community report select 의 content item */
data class CommunityReportSelect(
    @SerializedName("documentId")
    var documentId : Int,
    @SerializedName("title")
    var title : String,
    @SerializedName("state")
    var state : Int,
    @SerializedName("datetime")
    var datetime : String
)
