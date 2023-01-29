package com.umc.approval.data.dto.community.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**Community Item Dto*/
data class CommunityTok(

        @SerializedName("toktokId")
        var toktokId : Int,
        @SerializedName("profileImage")
        var profileImage : String,
        @SerializedName("nickname")
        var nickname : String,
        @SerializedName("level")
        var level : Int,
        @SerializedName("category")
        var category : Int,
        @SerializedName("content")
        var content : String,
        @SerializedName("voteTitle")
        var voteTitle : String,
        @SerializedName("voteIsEnd")
        var voteIsEnd : Boolean,
        @SerializedName("votePeople")
        var votePeople : Int,
        @SerializedName("voteIsSingle")
        var voteIsSingle : Boolean,
        @SerializedName("images")
        var images : List<String>,
        @SerializedName("link")
        var link : List<OpenGraphDto>,
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
)