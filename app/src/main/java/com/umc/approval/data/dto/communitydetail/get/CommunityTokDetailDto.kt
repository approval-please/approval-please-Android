package com.umc.approval.data.dto.communitydetail.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

data class CommunityTokDetailDto(

    @SerializedName("userId")
    var userId : Int,
    @SerializedName("profileImage")
    var profileImage : String?=null,
    @SerializedName("nickname")
    var nickname : String?="",
    @SerializedName("level")
    var level : Int,
    @SerializedName("toktokId")
    var toktokId : Int,
    @SerializedName("category")
    var category : Int,
    @SerializedName("content")
    var content : String?="",
    @SerializedName("link")
    var link : List<OpenGraphDto>?= listOf(),
    @SerializedName("tag")
    var tag : List<String>?= listOf(),
    @SerializedName("images")
    var images : List<String>?= listOf(),
    @SerializedName("voteId")
    var voteId : Int?=null,
    @SerializedName("voteTitle")
    var voteTitle : String?=null,
    @SerializedName("voteIsEnd")
    var voteIsEnd : Boolean?=null,
    @SerializedName("votePeople")
    var votePeople : Int?=null,
    @SerializedName("voteIsSingle")
    var voteIsSingle : Boolean?=null,
    @SerializedName("voteIsAnonymous")
    var voteIsAnonymous : Boolean?=false,
    @SerializedName("voteOption")
    var voteOption : List<VoteOption>?= listOf(),
    @SerializedName("voteSelect")
    var voteSelect : List<VoteOption>?=null,
    @SerializedName("votePeopleEachOption")
    var votePeopleEachOption : List<Int>?=null,
    @SerializedName("writerOrNot")
    var writerOrNot : Boolean?=false,
    @SerializedName("likedCount")
    var likedCount : Int,
    @SerializedName("likeOrNot")
    var likeOrNot : Boolean?= false,
    @SerializedName("followOrNot")
    var followOrNot : Boolean?= false,
    @SerializedName("scrapOrNot")
    var scrapOrNot : Boolean?= false,
    @SerializedName("commentCount")
    var commentCount : Int,
    @SerializedName("isModified")
    var isModified : Boolean?= false,
    @SerializedName("datetime")
    var datetime : String?= "50분전",
    @SerializedName("view")
    var view : Int,
    @SerializedName("scrapCount")
    var scrapCount : Int,
    @SerializedName("isNotification")
    var isNotification : Boolean?= false,
    )
