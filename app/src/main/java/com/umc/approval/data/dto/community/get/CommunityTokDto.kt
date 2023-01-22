package com.umc.approval.data.dto.community.get

import com.google.gson.annotations.SerializedName

/**Community Item Dto*/
data class CommunityTokDto (
        @SerializedName("communityTok")
        var communityTok : MutableList<CommunityTok> = mutableListOf<CommunityTok>()
)