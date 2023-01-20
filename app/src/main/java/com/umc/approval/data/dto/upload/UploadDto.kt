package com.umc.approval.data.dto.upload

/**
 * profile get or change dto
 * */
data class UploadDto (
        var title : String = "",
        var body : String = "",
        var category : String = "",
        var images : MutableList<String> = mutableListOf<String>(),
        var tags : MutableList<String> = mutableListOf<String>(),
        var link : String = "",
)