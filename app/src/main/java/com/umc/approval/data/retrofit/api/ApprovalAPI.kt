package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.approval.get.AgreeDto
import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.approval.get.DocumentDto
import com.umc.approval.data.dto.approval.post.AgreeMyPostDto
import com.umc.approval.data.dto.approval.post.AgreePostDto
import com.umc.approval.data.dto.comment.CommentListDto
import com.umc.approval.data.dto.upload.post.ApprovalUploadDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApprovalAPI {

    /**
     * @Post
     * @Get
     * ApprovalPaperDto
     * 결재서류 목록 조회 API
     * API 명세서 Check 완료
     */
    @GET("/documents")
    @Headers("content-type: application/json")
    fun getDocuments(@Query("category") category: String): Call<ApprovalPaperDto>

    /**
     * @Post
     * accessToken: 사용자 검증 토큰, 토큰이 없거나 유효하지 않으면 로그인 페이지로 이동
     * @Get
     * ApprovalPaperDto
     * 관심부서 결재서류 목록 조회 API
     * API 명세서 Check 완료
     */
    @GET("/documents/likes")
    @Headers("content-type: application/json")
    fun getInterestingCategoryDocuments(
        @Header("Authorization") accessToken: String, @Query("category") category: String
    ): Call<ApprovalPaperDto>

    /**
     * @Post
     * documentId: 개별 Id
     * @Get
     * ApprovalPaperDto
     * 관심부서 결재서류 목록 조회 API
     * API 명세서 Check 완료
     */
    @GET("/documents/{documentId}")
    @Headers("content-type: application/json")
    fun getDocumentDetail(
        @Path("documentId") documentId: String
    ): Call<DocumentDto>

    /**
     * @Post
     * accessToken : 사용자 검증 토큰
     * upload: 업로드할 Document 데이터
     * 서류 업로드 API
     * API 명세서 Check 완료
     * */
    @POST("/documents")
    @Headers("content-type: application/json")
    fun uploadDocument(
        @Header("Authorization") accessToken: String, @Body upload: ApprovalUploadDto
    ):Call<ApprovalUploadDto>

    /**
     * @Post
     * accessToken : 사용자 검증 토큰
     * upload: 업로드할 Document 데이터
     * 서류 업로드 API
     * API 명세서 Check 완료
     * */
    @DELETE("/documents/{documentId}")
    @Headers("content-type: application/json")
    fun deleteDocument(
        @Header("Authorization") accessToken: String, @Path("documentId") documentId: String
    ):Call<ResponseBody>

    /**
     * @Post
     * accessToken : 사용자 검증 토큰
     * upload: 업로드할 Document 데이터
     * 서류 업로드 API
     * API 명세서 Check 완료
     * */
    @POST("/documents/{documentId}")
    @Headers("content-type: application/json")
    fun agreeDocument(
        @Header("Authorization") accessToken: String, @Path("documentId") documentId: String, @Body agreePostDto: AgreePostDto
    ):Call<AgreeDto>

    /**
     * @Post
     * accessToken : 사용자 검증 토큰
     * 서류 업로드 API
     * API 명세서 Check 완료
     * */
    @POST("/approvals")
    @Headers("content-type: application/json")
    fun agreeMyDocument(
        @Header("Authorization") accessToken: String, @Body agreeMyPostDto: AgreeMyPostDto
    ):Call<ResponseBody>

    /**
     * @Post
     * documentId: 댓글 서류를 의미
     * @Get
     * CommentListDto
     * 댓글 리스트 API
     * API 명세서 Check 미완료
     */
    @GET("/documents/comments")
    @Headers("content-type: application/json")
    fun getComments(@Query("documentId") documentId: Int): Call<CommentListDto>
}