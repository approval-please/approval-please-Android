package com.umc.approval.data.dto.community
import com.umc.approval.data.dto.opengraph.OpenGraphDto

/**
 * Community Upload dto
 * */
data class TalkUploadDto (
        var body : String = "",
        var category : String = "",
        var images : MutableList<String> = mutableListOf<String>(),
        var tags : MutableList<String> = mutableListOf<String>(),
        var voteItems : MutableList<String> = mutableListOf<String>(),
        var opengraph : OpenGraphDto,
)