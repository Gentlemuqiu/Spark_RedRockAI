package com.example.lib.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceNewCreator(private val url: String) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //使用动态代理, 这里运用泛型,减少重复的代码
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    //内联函数和reified
    inline fun <reified T> create(): T = ServiceCreator.create(T::class.java)

}