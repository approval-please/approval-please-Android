package com.umc.approval.ui.retrofit.instance

import com.google.gson.GsonBuilder
import com.umc.approval.BuildConfig
import com.umc.approval.ui.retrofit.api.LoginAPI
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
            .client(okHttpClient).baseUrl(BuildConfig.LOCAL_BASE_URL).build() //build로 객체 생성
    }

    val serverApi: LoginAPI by lazy {
        retrofit.create(LoginAPI::class.java)
    }
}