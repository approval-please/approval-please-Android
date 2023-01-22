package com.umc.approval.data.dto.search.get


import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("community")
    val community: List<Community>,
    @SerializedName("documents")
    val documents: List<Document>,
    @SerializedName("users")
    val users: List<User>
)