package com.umc.approval.data.dto.community.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**Community Item Dto*/
data class CommunityTok(

        @SerializedName("toktokId")
        var toktokId : Int,
        @SerializedName("profileImage")
        var profileImage : String,
        @SerializedName("category")
        var category : Int,
        @SerializedName("userId")
        var userId : Int,
        @SerializedName("userLevel")
        var userLevel : Int,
        @SerializedName("nickname")
        var nickname : String,
        @SerializedName("content")
        var content : String,
        @SerializedName("images")
        var images : List<String>,
        @SerializedName("link")
        var link : OpenGraphDto,
        @SerializedName("tag")
        var tag : List<String>,
        @SerializedName("likeCount")
        var likeCount : Int,
        @SerializedName("commentCount")
        var commentCount : Int,
        @SerializedName("view")
        var view : Int,
        @SerializedName("datetime")
        val datetime: String,
        @SerializedName("vote")
        val voteDto: CommunityTokListVoteDto
)