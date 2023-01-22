package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.community.CommunityItemsDto
import com.umc.approval.data.dto.profile.CategoriesDto
import com.umc.approval.data.dto.upload.UploadDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Community  API
 * */
interface CommunityAPI {

    /**
     * @Post
     * accessToken : 사용자 검증 토큰
     * @Get
     * talk: profileImage, title, title_body, vote, vote_num,
     * report: images
     * common: type, nickname, rank, body, image, link, like,scrap, views, replys
     * */
    @GET("/community/items")
    @Headers("content-type: application/json")
    fun get_community_items(@Header("Authorization") accessToken: String):Call<CommunityItemsDto>
}