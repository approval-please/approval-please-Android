package com.umc.approval.data.dto.upload.post
import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**
 * Community Upload dto
 * */
data class TalkUploadDto (
        @SerializedName("category")
        var category : Int? = null,
        @SerializedName("content")
        var content : String? = null,
        @SerializedName("voteTitle")
        var voteTitle : String? = null,
        @SerializedName("voteIsSingle")
        var voteIsSingle : Boolean? = null,
        @SerializedName("voteIsAnonymous")
        var voteIsAnonymous : Boolean? = null,
        @SerializedName("voteOption")
        var voteOption : List<String>? = null,
        @SerializedName("link")
        var link : List<OpenGraphDto>? = null,
        @SerializedName("tag")
        var tag : List<String>? = null,
        @SerializedName("images")
        var images : List<String>? = null,
)