package com.umc.approval.data.dto.upload
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**
 * profile get or change dto
 * */
data class UploadDto (
        var title : String = "",
        var body : String = "",
        var category : String = "",
        var images : MutableList<String> = mutableListOf<String>(),
        var tags : MutableList<String> = mutableListOf<String>(),
        var opengraph : OpenGraphDto,
)