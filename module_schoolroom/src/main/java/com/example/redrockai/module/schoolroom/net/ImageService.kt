package com.example.redrockai.module.schoolroom.net

import com.example.redrockai.module.schoolroom.bean.ImageDateBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-05-21 19:11.
 * @Description : 生活很难，但总有人在爱你。
 */
interface ImageService {
    @GET("https://api.xygeng.cn/openapi/bing")
    fun getImage(): Call<ImageDateBean>

}