package com.umc.approval.data.dto.community.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**Community Item Dto*/
data class CommunityReport (

        @SerializedName("reportId")
        var reportId : Int,
        @SerializedName("userId")
        var userId : Int,
        @SerializedName("nickname")
        var nickname : String,
        @SerializedName("userLevel")
        var userLevel : Int,
        @SerializedName("content")
        var content : String,
        @SerializedName("images")
        var images : List<String>,
        @SerializedName("link")
        var link : OpenGraphDto,
        @SerializedName("tag")
        var tag : List<String>,
        @SerializedName("likedCount")
        var likedCount : Int,
        @SerializedName("commentCount")
        var commentCount : Int,
        @SerializedName("datetime")
        var datetime : String,
        @SerializedName("view")
        var view : Int,
        @SerializedName("document")
        var document : CommunityReportDocumentDto,
)