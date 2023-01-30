package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.approval.get.*
import com.umc.approval.data.dto.approval.post.AgreeMyPostDto
import com.umc.approval.data.dto.approval.post.AgreePostDto
import com.umc.approval.data.dto.upload.post.ApprovalUploadDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApprovalAPI {

    /**
     * 전체 결재서류 목록 조회 API
     * API 명세서 Check 완료
     * 반환값 설정 완료
     */
    @GET("/documents")
    @Headers("content-type: application/json")
    fun getDocuments(@Query("category") category: String?= null,
                     @Query("state") state: String?= null, @Query("sortBy") sortBy: String?= null): Call<ApprovalPaperDto>

    /**
     * 관심부서 결재서류 목록 조회 API
     * API 명세서 Check 완료
     * 반환값 설정 완료
     */
    @GET("/documents/likes")
    @Headers("content-type: application/json")
    fun getInterestingCategoryDocuments(
        @Header("Authorization") accessToken: String, @Query("category") category: String?= null,
        @Query("state") state: String?= null, @Query("sortBy") sortBy: String?= null
    ): Call<ApprovalPaperDto>

    /**
     * 결재서류 상세 보기
     * API 명세서 Check 완료
     * 반환값 설정 완료
     */
    @GET("/documents/{documentId}")
    @Headers("content-type: application/json")
    fun getDocumentDetail(
        @Path("documentId") documentId: String
    ): Call<DocumentDto>

    /**
     * 서류 업로드 API
     * API 명세서 Check 완료
     * 반환값 설정 완료
     * */
    @POST("/documents")
    @Headers("content-type: application/json")
    fun uploadDocument(
        @Header("Authorization") accessToken: String, @Body upload: ApprovalUploadDto
    ):Call<ResponseBody>

    /**
     * 서류 삭제 API
     * API 명세서 Check 완료
     * 반환값 설정 완료
     * */
    @DELETE("/documents/{documentId}")
    @Headers("content-type: application/json")
    fun deleteDocument(
        @Header("Authorization") accessToken: String, @Path("documentId") documentId: String
    ):Call<ResponseBody>

    /**
     * 타인 서류 승인 및 거절 API
     * API 명세서 Check 완료
     * 반환값 설정 완료
     * */
    @POST("/documents/{documentId}")
    @Headers("content-type: application/json")
    fun agreeDocument(
        @Header("Authorization") accessToken: String, @Path("documentId") documentId: String, @Body agreePostDto: AgreePostDto
    ):Call<AgreeDto>

    /**
     * 내 서류 승인 및 거절 API
     * API 명세서 Check 완료
     * 반환값 설정 완료
     * */
    @POST("/approvals")
    @Headers("content-type: application/json")
    fun agreeMyDocument(
        @Header("Authorization") accessToken: String, @Body agreeMyPostDto: AgreeMyPostDto
    ):Call<ResponseBody>

    /**
     * 관심부서 목록 API
     * API 명세서 Check 완료
     * 반환값 설정 완료
     * */
    @GET("/documents/likedCategory/my")
    @Headers("content-type: application/json")
    fun getMyCategory(
        @Header("Authorization") accessToken: String
    ):Call<InterestingDto>
}