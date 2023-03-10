package com.umc.approval.data.retrofit.instance

import com.google.gson.GsonBuilder
import com.umc.approval.API.LOCAL_BASE_URL
import com.umc.approval.data.retrofit.api.*
import com.umc.approval.data.retrofit.api.success.AccessTokenAPI
import com.umc.approval.data.retrofit.api.success.ApprovalAPI
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

    /**community api*/
    val communityApi: CommunityAPI by lazy {
        retrofit.create(CommunityAPI::class.java)
    }

    val ApprovalApi: ApprovalAPI by lazy {
        retrofit.create(ApprovalAPI::class.java)
    }

    val accessTokenAPI: AccessTokenAPI by lazy {
        retrofit.create(AccessTokenAPI::class.java)
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

    /**comment api*/
    val commentAPI : CommentAPI by lazy{
        retrofit.create(CommentAPI::class.java)
    }

    val mypageAPI : MyPageAPI by lazy{
        retrofit.create(MyPageAPI::class.java)
    }

    val searchAPI: SearchAPI by lazy {
        retrofit.create(SearchAPI::class.java)
    }
}
