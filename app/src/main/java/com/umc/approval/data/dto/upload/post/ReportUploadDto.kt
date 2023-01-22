package com.umc.approval.data.dto.upload.post
import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**
 * Community Upload dto
 * */
data class ReportUploadDto (

        @SerializedName("documentId")
        var documentId : Int,
        @SerializedName("content")
        var content : String,
        @SerializedName("opengraphs")
        var opengraphs : MutableList<OpenGraphDto>,
        @SerializedName("tags")
        var tags : MutableList<String> = mutableListOf<String>(),
        @SerializedName("images")
        var images : MutableList<String> = mutableListOf<String>(),
)