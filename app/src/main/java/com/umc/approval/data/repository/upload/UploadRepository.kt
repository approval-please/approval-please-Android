package com.umc.approval.data.repository.upload

import com.umc.approval.data.dto.upload.post.TalkUploadDto
import com.umc.approval.data.dto.profile.CategoriesDto
import com.umc.approval.data.dto.upload.post.ApprovalUploadDto
import com.umc.approval.data.dto.upload.post.ReportUploadDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.uploadApi
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Upload Repository
 * */
class UploadRepository() {

    /**tok upload api*/
    fun upload_tok_item(accessToken: String, toktok: TalkUploadDto):Call<ResponseBody> {
        return uploadApi.upload_community_tok(accessToken, toktok)
    }

    /**report upload api*/
    fun upload_report_item(accessToken: String, report: ReportUploadDto):Call<ResponseBody> {
        return uploadApi.upload_community_report(accessToken, report)
    }
}