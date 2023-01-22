package com.umc.approval.data.dto.upload.post
import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**
 * Community Upload dto
 * */
data class TalkUploadDto (
        @SerializedName("category")
        var category : Int,
        @SerializedName("content")
        var content : String,
        @SerializedName("voteTitle")
        var voteTitle : String,
        @SerializedName("voteIsSingle")
        var voteIsSingle : Boolean,
        @SerializedName("voteIsAnonymous")
        var voteIsAnonymous : Boolean,
        @SerializedName("voteOption")
        var voteOption : MutableList<String> = mutableListOf<String>(),
        @SerializedName("opengraphs")
        var opengraphs : MutableList<OpenGraphDto>,
        @SerializedName("tags")
        var tags : MutableList<String> = mutableListOf<String>(),
        @SerializedName("images")
        var images : MutableList<String> = mutableListOf<String>(),
)