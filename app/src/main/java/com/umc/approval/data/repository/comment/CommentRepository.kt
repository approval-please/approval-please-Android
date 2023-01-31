package com.umc.approval.data.repository.comment

import com.umc.approval.data.dto.comment.get.CommentListDto
import com.umc.approval.data.dto.comment.post.CommentPostDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.commentAPI
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Approval Fragment Repository
 */
class CommentRepository {

    fun getComments(accessToken: String?= null, documentId: String?=null,
                    toktokId : String?=null, reportId : String?=null): Call<CommentListDto> {
        return commentAPI.getComments(accessToken, documentId, toktokId, reportId)
    }

    fun postComments(accessToken: String, commentPostDto: CommentPostDto): Call<ResponseBody> {
        return commentAPI.postComment(accessToken, commentPostDto)
    }

    fun deleteComment(accessToken: String, commentId : String): Call<ResponseBody> {
        return commentAPI.deleteComment(accessToken, commentId)
    }
}