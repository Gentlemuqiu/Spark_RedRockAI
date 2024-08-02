package com.example.redrockai.module.schoolroom.net

import com.example.redrockai.module.schoolroom.bean.RelatedCategoryBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RelatedCategoryService {
    @GET("/videos/{tag_id}")
    fun getRelatedCategory(@Path("tag_id") tagId: Int): Call<RelatedCategoryBean>
}