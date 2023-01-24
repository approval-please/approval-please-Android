package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.data.dto.mypage.Profile
import com.umc.approval.data.dto.mypage.RecordDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MyPageAPI {

    @GET("/profile")
    @Headers("content-type: application/json")
    fun get_my_page(
        @Header("Authorization") accessToken : String) : Call<Profile>

    @GET("/profile/my/documents")
    @Headers("content-type: application/json")
    fun get_my_documment(
        @Header("Authorization") accessToken : String, @Query("state") state : Int) : Call<ApprovalPaperDto>

    @GET("/profile/{userId}/documents")
    @Headers("content-type: application/json")
    fun get_other_documment(
        @Header("Authorization") accessToken : String, @Query("state") state : Int, @Path("userId") userId:Int) : Call<ApprovalPaperDto>

    @GET("/profile/my/community/toktoks")
    @Headers("content-type: application/json")
    fun get_my_toktoks(
        @Header("Authorization") accessToken : String) : Call<CommunityTokDto>

    @GET("/profile/{userId}/community/toktoks")
    @Headers("content-type: application/json")
    fun get_other_toktoks(
        @Header("Authorization") accessToken : String, @Path("userId") userId:Int) : Call<CommunityTokDto>

    @GET("/profile/my/community/reports")
    @Headers("content-type: application/json")
    fun get_my_reports(
        @Header("Authorization") accessToken : String) : Call<CommunityReportDto>

    @GET("/profile/{userId}/community/reports")
    @Headers("content-type: application/json")
    fun get_my_reports(
        @Header("Authorization") accessToken : String, @Path("userId") userId:Int) : Call<CommunityReportDto>

    @GET("/profile/my/comments")
    @Headers("content-type: application/json")
    fun get_my_reply_document(
        @Header("Authorization") accessToken : String, @Query("postType") postType : Int) : Call<ApprovalPaperDto>

    @GET("/profile/my/comments")
    @Headers("content-type: application/json")
    fun get_my_reply_toktok(
        @Header("Authorization") accessToken : String, @Query("postType") postType : Int) : Call<CommunityTokDto>

    @GET("/profile/my/comments")
    @Headers("content-type: application/json")
    fun get_my_reply_reports(
        @Header("Authorization") accessToken : String, @Query("postType") postType : Int) : Call<CommunityReportDto>

    @GET("/profile/my/scraps")
    @Headers("content-type: application/json")
    fun get_my_scrap_document(
        @Header("Authorization") accessToken : String, @Query("postType") postType : Int) : Call<ApprovalPaperDto>

    @GET("/profile/my/scraps")
    @Headers("content-type: application/json")
    fun get_my_scrap_toktok(
        @Header("Authorization") accessToken : String, @Query("postType") postType : Int) : Call<CommunityTokDto>

    @GET("/profile/my/scraps")
    @Headers("content-type: application/json")
    fun get_my_scrap_reports(
        @Header("Authorization") accessToken : String, @Query("postType") postType : Int) : Call<CommunityReportDto>

    @GET("/profile/my/performances")
    @Headers("content-type: application/json")
    fun get_my_performances(
        @Header("Authorization") accessToken : String, @Path("userId") userId:Int) : Call<RecordDto>
}