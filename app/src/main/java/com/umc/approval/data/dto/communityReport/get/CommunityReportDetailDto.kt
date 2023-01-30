package com.umc.approval.data.dto.communityReport.get

import com.google.gson.annotations.SerializedName

/* 게시글 상세 조회 */
data class CommunityReportDetailDto(
    @SerializedName("userId")
    var userId : Int?,
    @SerializedName("profileImage")
    var profileImage : String?,
    @SerializedName("nickname")
    var nickname : String,
    @SerializedName("level")
    var level : Int,
    @SerializedName("documentId")
    var documentId : Int,
    @SerializedName("documentImageUrl")
    var documentImageUrl : String,
    @SerializedName("documentImageCount")
    var documentImageCount : Int,
    @SerializedName("documentCategory")
    var documentCategory : Int,
    @SerializedName("documentTitle")
    var documentTitle : String,
    @SerializedName("documentContent")
    var documentContent : String,
    @SerializedName("documentTag")
    var documentTag : List<String>,
    @SerializedName("reportId")
    var reportId : Int,
    @SerializedName("reportContent")
    var reportContent : String,
    @SerializedName("reportImageUrl")
    var reportImageUrl : String,
    @SerializedName("reportLink")
    var reportLink : List<CommunityReportLink>,
    @SerializedName("reportTag")
    var reportTag : List<String>,
    @SerializedName("likedCount")
    var likedCount : Int,
    @SerializedName("likeOrNot")
    var likeOrNot : Boolean?,
    @SerializedName("followOrNot")
    var followOrNot : Boolean?,
    @SerializedName("writerOrNot")
    var writerOrNot : Boolean?,
    @SerializedName("scrapOrNot")
    var scrapOrNot : Boolean?,
    @SerializedName("scrapCount")
    var scrapCount : Int,
    @SerializedName("commentCount")
    var commentCount : Int,
    @SerializedName("datetime")
    var datetime : String,
    @SerializedName("isModified")
    var isModified : Boolean,
    @SerializedName("view")
    var view : Int
)
