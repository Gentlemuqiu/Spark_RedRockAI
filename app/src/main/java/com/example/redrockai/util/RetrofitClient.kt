package com.example.redrockai.util

import com.example.redrockai.apiservice.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/8/2 20:21
 *  version : 1.0
 *  description :
 *  saying : 这世界天才那么多，也不缺我一个
 */
object RetrofitClient {
    private const val BASE_URL = "http://your.api.url"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}