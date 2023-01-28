package com.umc.approval.data.dto.community.get

import com.google.gson.annotations.SerializedName

/**Community Item Dto*/
data class CommunityTokDto (
        @SerializedName("content")
        var communityTok : List<CommunityTok>
)