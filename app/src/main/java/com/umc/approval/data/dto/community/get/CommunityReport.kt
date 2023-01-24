package com.umc.approval.data.dto.community.get

import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**Community Item Dto*/
data class CommunityReport (

        @SerializedName("nickname")
        var nickname : String,
        @SerializedName("level")
        var level : Int,
        @SerializedName("documentId")
        var documentId : Int,
        @SerializedName("documentState")
        var documentState : Int,
        @SerializedName("documentCategory")
        var documentCategory : Int,
        @SerializedName("documentTitle")
        var documentTitle : String,
        @SerializedName("documentContent")
        var documentContent : String,
        @SerializedName("documentTag")
        var documentTag : MutableList<String>,
        @SerializedName("reportContent")
        var reportContent : String,
        @SerializedName("imageUrl")
        var imageUrl : MutableList<String>,
        @SerializedName("linkUrl")
        var linkUrl : List<OpenGraphDto>,
        @SerializedName("tag")
        var tag : MutableList<String>,
        @SerializedName("view")
        var view : Int,
)