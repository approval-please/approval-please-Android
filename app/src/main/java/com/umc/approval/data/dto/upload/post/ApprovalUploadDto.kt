package com.umc.approval.data.dto.upload.post
import com.google.gson.annotations.SerializedName
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**
 * 서류 업로드 DTO
 * API 명세서 Check 완료
 * */
data class ApprovalUploadDto (
        @SerializedName("category")
        var category : Int? = null,
        @SerializedName("content")
        var content : String? = null,
        @SerializedName("title")
        var title : String? = null,
        @SerializedName("link")
        var opengraph : OpenGraphDto? = null,
        @SerializedName("tag")
        var tag : List<String>? = null,
        @SerializedName("images")
        var images : List<String>? = null,
)