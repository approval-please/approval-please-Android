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
        var link : List<OpenGraphDto>? = null,
        @SerializedName("tag")
        var tag : List<String>? = null,
        @SerializedName("images")
        var images : List<String>? = null,
)