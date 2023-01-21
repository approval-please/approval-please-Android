package com.umc.approval.data.repository.upload

import com.umc.approval.data.dto.community.TalkUploadDto
import com.umc.approval.data.dto.profile.CategoriesDto
import com.umc.approval.data.dto.upload.UploadDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.uploadApi
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Upload Repository
 * */
class UploadRepository() {

    /**카테고리 정보 로드 api*/
    fun get_categoires(accessToken: String):Call<CategoriesDto> {
        return uploadApi.get_categories(accessToken)
    }

    /**approval upload api*/
    fun upload_approval_item(accessToken: String, upload: UploadDto):Call<ResponseBody> {
        return uploadApi.upload_approval(accessToken, upload)
    }

    /**approval upload api*/
    fun upload_talk_item(accessToken: String, talkUpload: TalkUploadDto):Call<ResponseBody> {
        return uploadApi.upload_community_talk(accessToken, talkUpload)
    }
}