package com.umc.approval.data.repository.upload

import com.umc.approval.data.dto.upload.CategoriesDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.uploadApi
import retrofit2.Call

/**
 * Upload Repository
 * */
class UploadRepository() {

    /**카테고리 정보 로드 api*/
    fun get_categoires(accessToken: String):Call<CategoriesDto> {
        return uploadApi.get_categories(accessToken)
    }

}