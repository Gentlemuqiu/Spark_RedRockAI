package com.example.redrock.module.video.net

import com.example.redrock.module.video.Bean.RelatedCategoryBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RelatedService {
    @GET("/videos/{tag_id}")
    fun getRelated(@Path("tag_id") tagId: Int): Call<RelatedCategoryBean>
}