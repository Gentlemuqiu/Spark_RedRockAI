package com.example.redrockai.module.schoolroom.net


import com.example.redrockai.module.schoolroom.bean.CateGoryBean
import retrofit2.Call
import retrofit2.http.GET
interface CateGoryService {

    @GET("tags")
    fun getCateGory(): Call<CateGoryBean>
}