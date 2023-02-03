package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.data.dto.communityReport.get.CommunityReportDetailDto
import com.umc.approval.data.dto.communitydetail.get.CommunityItemDto
import com.umc.approval.data.dto.mypage.CommunityDto
import com.umc.approval.data.dto.communitydetail.get.CommunityTokDetailDto
import com.umc.approval.data.dto.communitydetail.post.CommunityVotePost
import com.umc.approval.data.dto.communitydetail.post.CommunityVoteResult
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
     * 톡톡 목록 조회 API
     * API 명세서 Check 완료
     * 반환값 설정 완료
     */
    @GET("/community/toktoks")
    @Headers("content-type: application/json")
    fun get_community_tok_items(@Header("Authorization") accessToken: String,
                                @Query("sortBy") sortBy: Int ?= null):Call<CommunityTokDto>

    /**
     * 리포트 목록 조회 API
     * API 명세서 Check 완료
     * 반환값 설정 완료
     */
    @GET("/community/reports")
    @Headers("content-type: application/json")
    fun get_community_report_items(@Header("Authorization") accessToken: String,
                                   @Query("sortBy") sortBy: Int ?= null):Call<CommunityReportDto>

    /**
     * 톡 업로드 API
     * API 명세서 Check 완료
     * 반환값 설정 완료
     */
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
        @Path("toktokId") toktokId : String,
    ): Call<CommunityTokDetailDto>

    /**
     * CommunityItemDto: 커뮤니티 포스트 정보(profileImage, nickname, level, isFollow) 리스트
     * 결재서류 좋아요 목록 조회 API
     */
    @GET("/community/reports/{reportId}")
    @Headers("content-type: application/json")
    fun get_community_report_detail(
        @Header("Authorization") accessToken: String,
        @Path("reportId") reportId : String,
    ): Call<CommunityReportDetailDto>

    /**
     * tok 삭제 API
     */
    @DELETE("/community/toktoks/{toktokId}")
    @Headers("content-type: application/json")
    fun delete_tok(
        @Header("Authorization") accessToken: String,
        @Path("toktokId") toktokId : String,
    ): Call<ResponseBody>

    /**
     * report 삭제 API
     */
    @DELETE("/community/reports/{reportId}")
    @Headers("content-type: application/json")
    fun delete_report(
        @Header("Authorization") accessToken: String,
        @Path("reportId") reportId : String,
    ): Call<ResponseBody>


    @POST("/community/toktoks/votes/{voteId}")
    @Headers("content-type: application/json")
    fun post_vote(
        @Header("Authorization") accessToken: String, @Path("voteId") voteId: String,
        @Body voteOptionIds : CommunityVotePost
    ):Call<CommunityVoteResult>


    /**
     * @Post
     * accessToken : 사용자 검증 토큰
     * postType: 게시글 유형
     * 커뮤니티 탭
     */
    @GET("/profile/my/community")
    @Headers("content-type: application/json")
    fun get_community(
        @Header("Authorization") accessToken : String,
        @Query("postType") postType: Int?=null) : Call<CommunityDto>
}