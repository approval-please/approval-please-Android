package com.umc.approval.data.dto.upload.post
import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**
 * Community Upload dto
 * */
data class ApprovalUploadDto (
        @SerializedName("category")
        var category : Int,
        @SerializedName("content")
        var content : String,
        @SerializedName("title")
        var title : String,
        @SerializedName("opengraph")
        var opengraph : OpenGraphDto,
        @SerializedName("tags")
        var tags : MutableList<String> = mutableListOf<String>(),
        @SerializedName("images")
        var images : MutableList<String> = mutableListOf<String>(),
)