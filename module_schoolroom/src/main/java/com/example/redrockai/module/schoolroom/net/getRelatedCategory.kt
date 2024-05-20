package com.example.redrockai.module.schoolroom.net

import com.example.redrockai.module.schoolroom.bean.RelatedCategoryBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface getRelatedCategory {
    @GET("api/v1/tag/videos?")
    fun getRelatedCategory(@Query("id") id: Int): Call<RelatedCategoryBean>
}