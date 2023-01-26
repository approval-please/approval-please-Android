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
     * upload : body, category, images, tags, voteItems, opengraph 정보
     * */
    @POST("/community/toktoks")
    @Headers("content-type: application/json")
    fun upload_community_tok(
        @Header("Authorization") accessToken: String, toktok: TalkUploadDto
    ):Call<ResponseBody>

    /**
     * @Post
     * accessToken : 사용자 검증 토큰
     * upload : body, category, images, tags, voteItems, opengraph 정보
     * */
    @POST("/community/reports")
    @Headers("content-type: application/json")
    fun upload_community_report(
        @Header("Authorization") accessToken: String, report: ReportUploadDto
    ):Call<ResponseBody>
}