package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.data.dto.communitydetail.get.CommunityItemDto
import com.umc.approval.data.dto.upload.post.ReportUploadDto
import com.umc.approval.data.dto.upload.post.TalkUploadDto
import okhttp3.ResponseBody
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
    fun get_community_tok_items(@Query("sortBy") sortBy: Int ?= null):Call<CommunityTokDto>

    /**
     * @Post
     * state : 최신 인기 팔로우 내 글
     * @Get
     * CommunityReportDto
     * */
    @GET("/community/reports")
    @Headers("content-type: application/json")
    fun get_community_report_items(@Query("sortBy") sortBy: Int ?= null):Call<CommunityReportDto>

    /**
     * @Get
     * accessToken: 사용자 검증 토큰
     * toktokId: 커뮤니티 포스트 ID
     * @Get
     * CommunityItemDto: 커뮤니티 포스트 정보(profileImage, nickname, level, isFollow) 리스트
     * 결재서류 좋아요 목록 조회 API
     */
    @GET("/community/toktoks/{toktokId}")
    @Headers("content-type: application/json")
    fun get_community_tok_detail(
        @Header("Authorization") accessToken: String,
        @Path("toktokId") toktokId : Int,
    ): Call<CommunityItemDto>

    /**
     * @Get
     * accessToken: 사용자 검증 토큰
     * reportId: 커뮤니티 포스트 ID
     * @Get
     * CommunityItemDto: 커뮤니티 포스트 정보(profileImage, nickname, level, isFollow) 리스트
     * 결재서류 좋아요 목록 조회 API
     */
    @GET("/community/reports/{reportId}")
    @Headers("content-type: application/json")
    fun get_community_report_detail(
        @Header("Authorization") accessToken: String,
        @Path("reportId") reportId : Int,
    ): Call<CommunityItemDto>


    /**
     * @Post
     * accessToken : 사용자 검증 토큰
     * upload : body, category, images, tags, voteItems, opengraph 정보
     * */
    @POST("/community/toktoks")
    @Headers("content-type: application/json")
    fun upload_community_tok(
        @Header("Authorization") accessToken: String, @Body toktok: TalkUploadDto
    ):Call<ResponseBody>

    /**
     * @Post
     * accessToken : 사용자 검증 토큰
     * upload : body, category, images, tags, voteItems, opengraph 정보
     * */
    @POST("/community/reports")
    @Headers("content-type: application/json")
    fun upload_community_report(
        @Header("Authorization") accessToken: String, @Body report: ReportUploadDto
    ):Call<ResponseBody>
}