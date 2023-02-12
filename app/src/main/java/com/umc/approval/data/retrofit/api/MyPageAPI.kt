package com.umc.approval.data.retrofit.api

import com.umc.approval.data.dto.approval.get.ApprovalPaperDto
import com.umc.approval.data.dto.community.get.CommunityReportDto
import com.umc.approval.data.dto.community.get.CommunityTokDto
import com.umc.approval.data.dto.mypage.*
import com.umc.approval.data.dto.mypage.CommunityDto
import com.umc.approval.data.dto.mypage.FollowListDto
import com.umc.approval.data.dto.mypage.Profile
import com.umc.approval.data.dto.mypage.RecordDto
import com.umc.approval.data.dto.otherpage.OtherCommunityDto
import com.umc.approval.data.dto.profile.ProfileChange
import com.umc.approval.data.dto.profile.ProfileContentDto
import com.umc.approval.data.dto.profile.ProfileDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MyPageAPI {

    /**
     * @Post
     * accessToken : 사용자 검증 토큰
     * 내 프로필 API
     * API 명세서 Check 완료
     * */
    @GET("/profile/my")
    @Headers("content-type: application/json")
    fun get_my_page(
        @Header("Authorization") accessToken : String) : Call<ProfileContentDto>

    /**
     * @Post
     * userId : 상대방 Id
     * 상대방 프로필 API
     * API 명세서 Check 완료
     * */
    @GET("/profile/{userId}")
    @Headers("content-type: application/json")
    fun get_other_page(@Path("userId") userId: Int) : Call<ProfileDto>

    /**
     * @Post
     * accessToken : 사용자 검증 토큰
     * 내 프로필 변경 API
     * API 명세서 Check 완료
     * */
    @PUT("/profile/update")
    @Headers("content-type: application/json")
    fun change_my_profile(
        @Header("Authorization") accessToken : String, @Body profileChange: ProfileChange) : Call<ResponseBody>


    /**내 서류 가지고 오기*/
    @GET("/profile/my/documents")
    @Headers("content-type: application/json")
    fun get_my_documment(
        @Header("Authorization") accessToken : String,
        @Query("state") state: Int?=null, @Query("isApproved") isApproved: Int?=null) : Call<ApprovalPaperDto>

    /* 다른 사람 서류 가지고 오기*/
    @GET("/profile/{userId}/documents")
    @Headers("content-type: application/json")
    fun get_other_documment(@Path("userId") userId: Int, @Query("state") state: Int?=null) : Call<ApprovalPaperDto>

    /* 다른 사람 커뮤니티(톡톡, 보고서) 가지고 오기 */
    @GET("/profile/{userId}/community")
    @Headers("content-type: application/json")
    fun get_other_community(@Path("userId") userId : Int, @Query("postType") postType: Int? = null) : Call<OtherCommunityDto>

    /**내 실적 가지고 오기*/
    @GET("/profile/my/performances")
    @Headers("content-type: application/json")
    fun get_my_performances(
        @Header("Authorization") accessToken : String) : Call<RecordDto>

    /* 내 댓글 목록 가져오기 */
    @GET("/profile/my/comments")
    @Headers("content-type: application/json")
    fun get_my_comments(
        @Header("Authorization") accessToken : String, @Query("postType") postType : Int? = null, @Query("state") state : Int? = null
    ) : Call<MyCommentDto>

    /* 내 스크랩 목록 가져오기 */
    @GET("/profile/my/scraps")
    @Headers("content-type: application/json")
    fun get_my_scraps(
        @Header("Authorization") accessToken : String, @Query("postType") postType : Int? = null, @Query("state") state : Int? = null
    ) : Call<MyScrapDto>
}