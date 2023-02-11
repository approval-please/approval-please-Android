package com.umc.approval.data.repository.mypage

import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.mypage.*
import com.umc.approval.data.dto.community.get.CommunityTok
import com.umc.approval.data.dto.mypage.CommunityDto
import com.umc.approval.data.dto.mypage.FollowListDto
import com.umc.approval.data.dto.mypage.Profile
import com.umc.approval.data.dto.mypage.RecordDto
import com.umc.approval.data.dto.profile.ProfileChange
import com.umc.approval.data.dto.profile.ProfileContentDto
import com.umc.approval.data.dto.profile.ProfileDto
import com.umc.approval.data.retrofit.instance.RetrofitInstance
import com.umc.approval.data.retrofit.instance.RetrofitInstance.mypageAPI
import okhttp3.ResponseBody
import retrofit2.Call

class MyPageFragmentRepository {

    fun get_my_page(accessToken : String) : Call<ProfileContentDto>{
        return mypageAPI.get_my_page(accessToken)
    }

    fun change_my_profile(accessToken : String, profileChange: ProfileChange) : Call<ResponseBody>{
        return mypageAPI.change_my_profile(accessToken, profileChange)
    }

    fun get_my_documents(accessToken : String, state: Int?=null, isApproved: Int?=null) : Call<ApprovalPaperDto>{
        return mypageAPI.get_my_documment(accessToken, state, isApproved)
    }

    /* OtherPageActivityRepository로 이동
    fun get_other_documents(accessToken : String, userId: Int, state: Int?=null, isApproved: Int?=null) : Call<ApprovalPaperDto>{
        return mypageAPI.get_other_documment(accessToken, userId, state, isApproved)
    }
    */

    fun get_my_perfoemances(accessToken : String) : Call<RecordDto>{
        return mypageAPI.get_my_performances(accessToken)
    }

    fun get_my_comments(accessToken : String, postType : Int?, state : Int?) : Call<MyCommentDto> {
        return mypageAPI.get_my_comments(accessToken, postType, state)
    }

    fun get_my_scraps(accessToken : String, postType : Int?, state : Int?) : Call<MyScrapDto>{
        return mypageAPI.get_my_scraps(accessToken, postType, state)
    }
}