package com.umc.approval.data.dto.search.post

import com.google.gson.annotations.SerializedName

data class SearchDto (
        @SerializedName("query")
        val query : String,
        @SerializedName("postType")
        var postType : Int = -1, //default: 통합 서류
        @SerializedName("category")
        var category : Int = -1, //default: 모든 부서
        @SerializedName("state")
        var state : Int = -1, //default: 결재서류만, 전체(승인, 반려, 진행중)
        @SerializedName("sortBy")
        var sortBy : Int = -1, //default: 정확도
        @SerializedName("category")
        var page : Int = -1, //default: 모든 부서
)