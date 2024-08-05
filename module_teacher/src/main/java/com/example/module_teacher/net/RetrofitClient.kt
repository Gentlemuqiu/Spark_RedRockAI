package com.example.module_teacher.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://47.108.48.10:8080"
    val retryInterceptor = RetryInterceptor(maxRetries = 3) // 设置最大重试次数

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(9000, TimeUnit.SECONDS)
        .readTimeout(9000, TimeUnit.SECONDS)
        .writeTimeout(9000, TimeUnit.SECONDS)
        .addInterceptor(retryInterceptor)
        .build()

    val instance: Retrofit by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
