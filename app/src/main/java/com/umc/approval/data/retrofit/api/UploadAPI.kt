package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.profile.CategoriesDto
import com.umc.approval.data.dto.upload.UploadDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Upload and Community Upload API
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
        @Query("upload") upload: UploadDto):Call<ResponseBody>


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