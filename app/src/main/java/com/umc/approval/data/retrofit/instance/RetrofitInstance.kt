package com.umc.approval.data.retrofit.instance

import com.google.gson.GsonBuilder
import com.umc.approval.API.LOCAL_BASE_URL
import com.umc.approval.data.retrofit.api.CommunityAPI
import com.umc.approval.data.retrofit.api.ProfileAPI
import com.umc.approval.data.retrofit.api.UploadAPI
import com.umc.approval.data.retrofit.api.HomeAPI
import com.umc.approval.data.retrofit.api.ApprovalAPI
import com.umc.approval.data.retrofit.api.ParticipantAPI
import com.umc.approval.data.retrofit.api.LikeAPI
import com.umc.approval.data.retrofit.api.FollowAPI
import com.umc.approval.data.retrofit.api.LoginAPI
import com.umc.approval.data.retrofit.api.MyPageAPI
import com.umc.approval.data.retrofit.api.NotificationAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val okHttpClient: OkHttpClient by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
    }

    private val retrofit: Retrofit by lazy {

        val gson = GsonBuilder().setLenient().create()

        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient).baseUrl(LOCAL_BASE_URL).build() //build로 객체 생성
    }

    /**login api*/
    val serverApi: LoginAPI by lazy {
        retrofit.create(LoginAPI::class.java)
    }

    /**profile api*/
    val profileApi: ProfileAPI by lazy {
        retrofit.create(ProfileAPI::class.java)
    }

    /**upload api*/
    val uploadApi: UploadAPI by lazy {
        retrofit.create(UploadAPI::class.java)
    }

    /**community api*/
    val communityApi: CommunityAPI by lazy {
        retrofit.create(CommunityAPI::class.java)
    }

    val HomeApi: HomeAPI by lazy {
        retrofit.create(HomeAPI::class.java)
    }

    val ApprovalApi: ApprovalAPI by lazy {
        retrofit.create(ApprovalAPI::class.java)
    }

    val ParticipantApi: ParticipantAPI by lazy {
        retrofit.create(ParticipantAPI::class.java)
    }

    val LikeApi: LikeAPI by lazy {
        retrofit.create(LikeAPI::class.java)
    }

    val notificationApi : NotificationAPI by lazy{
        retrofit.create(NotificationAPI::class.java)
    }

    /**follow api*/
    val followApi : FollowAPI by lazy{
        retrofit.create(FollowAPI::class.java)
    }

    val mypageAPI : MyPageAPI by lazy{
        retrofit.create(MyPageAPI::class.java)
    }
}
