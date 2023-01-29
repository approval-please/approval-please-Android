package com.umc.approval.data.dto.community.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**Community Item Dto*/
data class CommunityReport (

        @SerializedName("reportId")
        var reportId : Int,
        @SerializedName("documentId")
        var documentId : Int,
        @SerializedName("nickname")
        var nickname : String,
        @SerializedName("level")
        var level : Int,
        @SerializedName("documentTitle")
        var documentTitle : String,
        @SerializedName("documentContent")
        var documentContent : String,
        @SerializedName("documentImageUrl")
        var documentImageUrl : List<String>,
        @SerializedName("documentTag")
        var documentTag : List<String>,
        @SerializedName("reportContent")
        var reportContent : String,
        @SerializedName("reportImageUrl")
        var reportImageUrl : List<String>,
        @SerializedName("reportLink")
        var reportLink : List<OpenGraphDto>,
        @SerializedName("reportTag")
        var reportTag : List<String>,
        @SerializedName("likedCount")
        var likedCount : Int,
        @SerializedName("likeOrNot")
        var likeOrNot : Boolean,
        @SerializedName("followOrNot")
        var followOrNot : Boolean,
        @SerializedName("scrapCount")
        var scrapCount : Int,
        @SerializedName("commentCount")
        var commentCount : Int,
        @SerializedName("datetime")
        var datetime : String,
        @SerializedName("view")
        var view : Int,
)