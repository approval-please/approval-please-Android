package com.umc.approval.data.dto.community.get

import com.google.gson.annotations.SerializedName

/**Community Item Dto*/
data class CommunityReportDto (
        @SerializedName("content")
        var communityReport : List<CommunityReport>
)