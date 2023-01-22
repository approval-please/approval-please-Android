package com.umc.approval.data.dto.search.get


import com.google.gson.annotations.SerializedName

data class Vote(
    @SerializedName("isAnonymous")
    val isAnonymous: Boolean,
    @SerializedName("isEnd")
    val isEnd: Boolean,
    @SerializedName("isSingle")
    val isSingle: Boolean,
    @SerializedName("questions")
    val questions: List<String>,
    @SerializedName("voteId")
    val voteId: Int,
    @SerializedName("voteUserCount")
    val voteUserCount: Int
)