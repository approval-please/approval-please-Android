package com.umc.approval.data.dto.community.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**Community Item Dto*/
data class CommunityTokListVoteDto(

        @SerializedName("title")
        var title : String,
        @SerializedName("isEnd")
        var isEnd : Boolean,
        @SerializedName("voteUserCount")
        var voteUserCount : Int,
        @SerializedName("isSingle")
        var isSingle : Boolean,
        @SerializedName("isAnonymous")
        var isAnonymous : Boolean,
        @SerializedName("voteId")
        var voteId : Int,
)