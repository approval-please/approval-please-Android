package com.umc.approval.data.dto.community.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**Community Item Dto*/
data class CommunityTok (
//
//        @SerializedName("profileImage")
//        var profileImage : String,
//        @SerializedName("rank")
//        var rank : String,
//        @SerializedName("reply")
//        var reply : Int,
//        @SerializedName("like")
//        var like : Int,
//        @SerializedName("scrap")
//        var scrap : Int,
//        @SerializedName("updatedAt")
//        val updatedAt: String,

        @SerializedName("nickname")
        var nickname : String,
        @SerializedName("category")
        var category : Int,
        @SerializedName("content")
        var content : String,
        @SerializedName("voteTitle")
        var voteTitle : String,
        @SerializedName("voteIsSingle")
        var voteIsSingle : Int,
        @SerializedName("voteIsAnnoymous")
        var voteIsAnnoymous : Boolean,
        @SerializedName("voteIsEnd")
        var voteIsEnd : Int,
        @SerializedName("imageUrl")
        var imageUrl : List<String>,
        @SerializedName("linkUrl")
        var linkUrl : List<OpenGraphDto>,
        @SerializedName("tags")
        var tags : List<String>,
        @SerializedName("view")
        var view : Int,
)