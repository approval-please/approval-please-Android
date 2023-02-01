package com.umc.approval.data.dto.communitydetail.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

data class CommunityTokDetailDto(

    @SerializedName("userId")
    var userId : Int?,
    @SerializedName("profileImage")
    var profileImage : String?,
    @SerializedName("nickname")
    var nickname : String,
    @SerializedName("level")
    var level : Int,
    @SerializedName("toktokId")
    var toktokId : Int,
    @SerializedName("category")
    var category : Int,
    @SerializedName("content")
    var content : String,
    @SerializedName("link")
    var link : List<OpenGraphDto>?,
    @SerializedName("tag")
    var tag : List<String>?,
    @SerializedName("images")
    var images : List<String>?,
    @SerializedName("voteId")
    var voteId : Int?,
    @SerializedName("voteTitle")
    var voteTitle : String?,
    @SerializedName("voteIsEnd")
    var voteIsEnd : Boolean?,
    @SerializedName("votePeople")
    var votePeople : Int?,
    @SerializedName("voteIsSingle")
    var voteIsSingle : Boolean?,
    @SerializedName("voteIsAnonymous")
    var voteIsAnonymous : Boolean?,
    @SerializedName("voteOption")
    var voteOption : List<VoteOption>?,
    @SerializedName("voteSelect")
    var voteSelect : List<VoteOption>?,
    @SerializedName("votePeopleEachOption")
    var votePeopleEachOption : List<Int>?,
    @SerializedName("writerOrNot")
    var writerOrNot : Boolean?,
    @SerializedName("likedCount")
    var likedCount : Int,
    @SerializedName("likeOrNot")
    var likeOrNot : Boolean?,
    @SerializedName("followOrNot")
    var followOrNot : Boolean?,
    @SerializedName("scrapOrNot")
    var scrapOrNot : Boolean?,
    @SerializedName("commentCount")
    var commentCount : Int,
    @SerializedName("isModified")
    var isModified : Boolean,
    @SerializedName("datetime")
    var datetime : String,
    @SerializedName("view")
    var view : Int,

    )

data class VoteOption(
    @SerializedName("voteOptionId")
    val voteOptionId: String,
    @SerializedName("opt")
    val opt: String,
)
