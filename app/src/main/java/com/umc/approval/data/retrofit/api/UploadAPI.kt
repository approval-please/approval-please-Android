package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.upload.post.TalkUploadDto
import com.umc.approval.data.dto.profile.CategoriesDto
import com.umc.approval.data.dto.upload.post.ApprovalUploadDto
import com.umc.approval.data.dto.upload.post.ReportUploadDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Approval Upload and Community Upload API
 * */
interface UploadAPI {

    /**
     * @Post
     * accessToken : 사용자 검증 토큰
     * upload : title, body, images, tags, link 정보
     * approval upload api
     * */
    @POST("/upload/approval")
    @Headers("content-type: application/json")
    fun upload_approval(
        @Header("Authorization") accessToken: String,
        @Query("upload") upload: ApprovalUploadDto):Call<ResponseBody>

    /**
     * @Post
     * accessToken : 사용자 검증 토큰
     * upload : body, category, images, tags, voteItems, opengraph 정보
     * */
    @POST("/community/toktoks")
    @Headers("content-type: application/json")
    fun upload_community_tok(
        @Header("Authorization") accessToken: String,
        @Query("toktok") toktok: TalkUploadDto
    ):Call<ResponseBody>

    /**
     * @Post
     * accessToken : 사용자 검증 토큰
     * upload : body, category, images, tags, voteItems, opengraph 정보
     * */
    @POST("/community/reports")
    @Headers("content-type: application/json")
    fun upload_community_report(
        @Header("Authorization") accessToken: String,
        @Query("report") report: ReportUploadDto
    ):Call<ResponseBody>

    /**
     * @Post
     * accessToken : 사용자 검증 토큰
     * @Get
     * profile : nickname, introduce, image 정보
     * category 정보 로드
     * */
    // 굳이 필요한지 잘 모르겠음, 카테고리는 한정적 아닌가???
    @GET("/upload/categories")
    @Headers("content-type: application/json")
    fun get_categories(@Header("Authorization") accessToken: String):Call<CategoriesDto>
}