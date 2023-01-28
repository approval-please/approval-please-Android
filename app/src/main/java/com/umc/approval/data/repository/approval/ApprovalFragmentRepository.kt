package com.umc.approval.data.repository.approval

import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.approval.get.DocumentDto
import com.umc.approval.data.dto.comment.CommentListDto
import com.umc.approval.data.dto.comment.DocumentCommentDto
import com.umc.approval.data.dto.upload.post.ApprovalUploadDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.ApprovalApi
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Approval Fragment Repository
 */
class ApprovalFragmentRepository {

    /**
     * 결재서류 목록 조회 API
     */
    fun getDocuments(category: String): Call<ApprovalPaperDto> {
        return ApprovalApi.getDocuments(category)
    }

    /**
     * 관심부서 결재서류 목록 조회 API
     */
    fun getInterestingCategoryDocuments(accessToken: String, category: String): Call<ApprovalPaperDto> {
        return ApprovalApi.getInterestingCategoryDocuments(accessToken, category)
    }

    /**
     * 결재서류 상세 조회 API
     */
    fun getDocumentDetail(documentId: String): Call<DocumentDto> {
        return ApprovalApi.getDocumentDetail(documentId)
    }

    /**
     * 댓글 목록 API
     */
    fun getComments(documentId: Int): Call<CommentListDto> {
        return ApprovalApi.getComments(documentId)
    }

    /**
     * 결재서류 업로드 API
     */
    fun postDocument(accessToken: String, upload: ApprovalUploadDto): Call<ApprovalUploadDto> {
        return ApprovalApi.uploadDocument(accessToken, upload)
    }

    /**
     * 결재서류 업로드 API
     */
    fun deleteDocument(accessToken: String, documentId: String): Call<ResponseBody> {
        return ApprovalApi.deleteDocument(accessToken, documentId)
    }
}