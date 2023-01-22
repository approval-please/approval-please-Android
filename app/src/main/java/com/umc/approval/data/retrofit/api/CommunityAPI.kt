package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.community.get.CommunityTokDto
import retrofit2.Call
import retrofit2.http.*

/**
 * Community API
 * */
interface CommunityAPI {

    /**
     * @Post
     * state : 최신 인기 팔로우 내 글
     * @Get
     * CommunityTalkDto
     * */
    @GET("/community/toktoks")
    @Headers("content-type: application/json")
    fun get_community_tok_items(@Query("state") state: Int):Call<CommunityTokDto>

    /**
     * @Post
     * state : 최신 인기 팔로우 내 글
     * @Get
     * CommunityReportDto
     * */
    @GET("/community/reports")
    @Headers("content-type: application/json")
    fun get_community_report_items(@Query("state") state: Int):Call<CommunityReportDto>
}