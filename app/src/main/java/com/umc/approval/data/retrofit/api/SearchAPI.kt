package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.search.get.Community
import com.umc.approval.data.dto.search.get.Document
import com.umc.approval.data.dto.search.get.SearchResponse
import com.umc.approval.data.dto.search.get.User
import com.umc.approval.data.dto.search.post.SearchDto
import retrofit2.Call
import retrofit2.http.*

/**
 * Search API
 * */
interface SearchAPI {

    //opengraph로 받을 것, 링크는 추가 작업이 필요

    /**
     * @Post
     * SearchDto : query, postType = -1, category, state, sortBy, page
     * @Get
     * Success:
     * */
    @POST("/search")
    @Headers("content-type: application/json")
    fun search_total(@Query("searchDto") searchDto: SearchDto):Call<SearchResponse>


    /**
     * @Post
     * SearchDto : query, postType = 0, category, state, sortBy, page
     * @Get
     * Success:
     * */
    @POST("/search")
    @Headers("content-type: application/json")
    fun search_document(@Query("searchDto") searchDto: SearchDto):Call<Document>


    /**
     * @Post
     * SearchDto : query, postType = 1, category, state, sortBy, page
     * @Get
     * Success:
     * */
    @POST("/search")
    @Headers("content-type: application/json")
    fun search_community(@Query("searchDto") searchDto: SearchDto):Call<Community>


    /**
     * @Post
     * SearchDto : query, postType = 2, category, state, sortBy, page
     * @Get
     * Success:
     * */
    @POST("/search")
    @Headers("content-type: application/json")
    fun search_user(@Query("searchDto") searchDto: SearchDto):Call<User>
}