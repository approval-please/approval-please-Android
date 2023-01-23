package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.common.CommonUserListDto
import retrofit2.Call
import retrofit2.http.*

/**
 * Search API
 * */
interface SearchAPI {

    //postType 0, tok
    @GET("/search")
    @Headers("content-type: application/json")
    fun search_documment(@Query("query") query: String,
                         @Query("postType") postType: String,
                         @Query("category") category: String,
                         @Query("state") state : Int,
                         @Query("sortBy") sortBy : Int,
                         @Query("page") page : Int) : Call<ApprovalPaperDto>

    //postType 1-1, tok
    @GET("/search")
    @Headers("content-type: application/json")
    fun search_community_tok(@Query("query") query: String,
                         @Query("postType") postType: String,
                         @Query("category") category: String,
                         @Query("state") state : Int,
                         @Query("sortBy") sortBy : Int,
                         @Query("page") page : Int) : Call<ApprovalPaperDto>

    //postType 1-2, report
    @GET("/search")
    @Headers("content-type: application/json")
    fun search_community_report(@Query("query") query: String,
                         @Query("postType") postType: String,
                         @Query("category") category: String,
                         @Query("state") state : Int,
                         @Query("sortBy") sortBy : Int,
                         @Query("page") page : Int) : Call<ApprovalPaperDto>

    //postType 2
    @GET("/search")
    @Headers("content-type: application/json")
    fun search_user(@Query("query") query: String,
                         @Query("postType") postType: String,
                         @Query("category") category: String,
                         @Query("state") state : Int,
                         @Query("sortBy") sortBy : Int,
                         @Query("page") page : Int) : Call<CommonUserListDto>
}