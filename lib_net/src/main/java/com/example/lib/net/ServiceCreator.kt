package com.example.lib.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private const val BASE_URL = "https://baobab.kaiyanapp.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //使用动态代理, 这里运用泛型,减少重复的代码
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)


}