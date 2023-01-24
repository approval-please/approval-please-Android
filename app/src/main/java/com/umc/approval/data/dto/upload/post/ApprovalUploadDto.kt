package com.umc.approval.data.dto.upload.post
import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**
 * Document Upload dto
 * */
data class ApprovalUploadDto (
        @SerializedName("category")
        var category : Int,
        @SerializedName("content")
        var content : String,
        @SerializedName("title")
        var title : String,
        @SerializedName("link")
        var opengraph : OpenGraphDto? = null,
        @SerializedName("tag")
        var tag : List<String>? = null,
        @SerializedName("images")
        var images : List<String>? = null,
)