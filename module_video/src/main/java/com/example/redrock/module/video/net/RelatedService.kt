package com.example.redrock.module.video.net

import com.example.redrock.module.video.Bean.Related
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RelatedService {
    @GET("api/v4/video/related?")
    fun getRelated(@Query("id") id: Int): Call<Related>
}