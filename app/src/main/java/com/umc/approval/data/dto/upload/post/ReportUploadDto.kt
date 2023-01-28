package com.umc.approval.data.dto.upload.post
import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**
 * Community Upload dto
 * */
data class ReportUploadDto (

        @SerializedName("documentId")
        var documentId : Int? = null,
        @SerializedName("content")
        var content : String? = null,
        @SerializedName("link")
        var link : MutableList<OpenGraphDto>? = null,
        @SerializedName("tag")
        var tag : MutableList<String>? = null,
        @SerializedName("images")
        var images : MutableList<String>? = null,
)