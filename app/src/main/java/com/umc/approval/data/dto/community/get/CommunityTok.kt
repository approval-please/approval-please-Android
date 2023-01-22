package com.umc.approval.data.dto.community.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**Community Item Dto*/
data class CommunityTok (

        @SerializedName("nickname")
        var nickname : String,
        @SerializedName("profileImage")
        var profileImage : String,
        @SerializedName("rank")
        var rank : String,
        @SerializedName("body")
        var body : String,
        @SerializedName("isVote")
        var isVote : Boolean,
        @SerializedName("voteParticipants")
        var voteParticipants : Int,
        @SerializedName("image")
        var image : String,
        @SerializedName("tags")
        var tags : MutableList<String> = mutableListOf<String>(),
        @SerializedName("opengraph")
        var opengraph : OpenGraphDto,
        @SerializedName("like")
        var like : Int,
        @SerializedName("scrap")
        var scrap : Int,
        @SerializedName("view")
        var view : Int,
        @SerializedName("reply")
        var reply : Int,
        @SerializedName("updatedAt")
        val updatedAt: String,
)