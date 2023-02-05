package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.common.CommonUserListDto
import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.community.get.CommunityTokDto
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
    @GET("/community/toktoks/search")
    @Headers("content-type: application/json")
    fun search_community_tok(
                    @Query("query") query: String,
                    @Query("isTag") isTag : Int,
                    @Query("category") category: Int?=null,
                    @Query("sortBy") sortBy : Int,
    ):Call<CommunityTokDto>

    //postType 1-2, report
    @GET("/community/reports/search")
    @Headers("content-type: application/json")
    fun search_community_report(
                    @Query("query") query: String,
                    @Query("isTag") isTag : Int,
                    @Query("category") category: Int?=null,
                    @Query("sortBy") sortBy : Int,
    ):Call<CommunityReportDto>

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