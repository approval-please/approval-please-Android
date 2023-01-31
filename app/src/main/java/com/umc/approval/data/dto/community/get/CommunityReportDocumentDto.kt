package com.umc.approval.data.dto.community.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.approval.get.ApprovalPaper
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**Community Item Dto*/
data class CommunityReportDocumentDto (

        @SerializedName("documentId")
        var documentId : Int,
        @SerializedName("title")
        var title : String,
        @SerializedName("content")
        var content : String,
        @SerializedName("thumbnailImage")
        var thumbnailImage : String,
        @SerializedName("tag")
        var tag : List<String>,
        @SerializedName("imageCount")
        var imageCount : Int,
)