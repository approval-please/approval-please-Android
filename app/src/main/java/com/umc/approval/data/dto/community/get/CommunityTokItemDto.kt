package com.umc.approval.data.dto.community.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto


data class CommunityTokItemDto(

    @SerializedName("toktokId")
    var toktokId : Int,
    @SerializedName("category")
    var category : Int,
    @SerializedName("userId")
    var userId : Int,
    @SerializedName("nickname")
    var nickname : String,
    @SerializedName("userLevel")
    var userLevel : Int,
    @SerializedName("profileImage")
    var profileImage : String,
    @SerializedName("likeCount")
    var likeCount : Int,
    @SerializedName("commentCount")
    var commentCount : Int,
    @SerializedName("view")
    var view : Int,
    @SerializedName("content")
    var content : String,
    @SerializedName("tag")
    var tag : List<String>?,
    @SerializedName("images")
    var images : List<String>,
    @SerializedName("link")
    var link : List<OpenGraphDto>?,
    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("vote")
    val vote: CommunityTokListVoteDto?,

)