package com.umc.approval.data.repository.search

import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.data.dto.search.get.SearchUserDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.searchAPI
import com.umc.approval.util.ApprovalPaper
import retrofit2.Call

class SearchFragmentApiRepository {

    fun search_documents(query: String, isTag: Int, category: Int ?= null, state:Int?=null, sortBy: Int): Call<ApprovalPaperDto> {
        return searchAPI.search_documents(query, isTag, category, state, sortBy)
    }

    fun search_community_tok(query:String, isTag: Int, category: Int?= null, sortBy: Int): Call<CommunityTokDto> {
        return searchAPI.search_community_tok(query, isTag, category, sortBy)
    }

    fun search_community_report(query: String, isTag: Int, category: Int ?= null, sortBy: Int): Call<CommunityReportDto> {
        return searchAPI.search_community_report(query, isTag, category, sortBy)
    }

    fun search_user(query: String): Call<SearchUserDto> {
        return searchAPI.search_user(query)
    }
}