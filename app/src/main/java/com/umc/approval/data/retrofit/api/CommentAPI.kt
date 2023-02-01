package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.comment.get.CommentListDto
import com.umc.approval.data.dto.comment.post.CommentPostDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface CommentAPI {

    @GET("/comments")
    @Headers("content-type: application/json")
    fun getComments(
                    @Header("Authorization") accessToken: String?= null,
                    @Query("documentId") documentId: String?=null,
                    @Query("toktokId") toktokId: String?=null,
                    @Query("reportId") reportId: String?=null): Call<CommentListDto>

    @POST("/comments")
    @Headers("content-type: application/json")
    fun postComment(@Header("Authorization") accessToken: String, @Body commentPostDto: CommentPostDto): Call<ResponseBody>

    @DELETE("/comments/{commentId}")
    @Headers("content-type: application/json")
    fun deleteComment(@Header("Authorization") accessToken: String, @Path("commentId") commentId : String): Call<ResponseBody>
}