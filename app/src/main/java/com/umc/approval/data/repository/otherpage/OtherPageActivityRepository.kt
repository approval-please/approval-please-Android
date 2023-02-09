package com.umc.approval.data.repository.otherpage

import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.otherpage.OtherCommunityDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance.mypageAPI
import com.umc.approval.data.dto.profile.ProfileDto
import retrofit2.Call

class OtherPageActivityRepository {
    fun get_other_page(userId : Int) : Call<ProfileDto>{
        return mypageAPI.get_other_page(userId)
    }
    fun get_other_document(userId : Int, state : Int? = null) : Call<ApprovalPaperDto>{
        return mypageAPI.get_other_documment(userId, state)
    }
    fun get_other_community(userId : Int, postType : Int? = null) : Call<OtherCommunityDto>{
        return mypageAPI.get_other_community(userId, postType)
    }
}