package com.example.redrockai.module.schoolroom.net

import com.example.lib.net.ServiceCreator
import com.example.redrockai.module.schoolroom.bean.CateGoryBean

import retrofit2.Call
import retrofit2.http.GET
interface CateGoryService {
    companion object {
        inline fun <reified T> create(): T = ServiceCreator.create(T::class.java)
    }

    @GET("api/v4/categories")
    fun getCateGory(): Call<CateGoryBean>
}