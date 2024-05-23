package com.example.redrockai.module.schoolroom.net

import com.example.redrockai.module.schoolroom.bean.SentenceBean
import retrofit2.Call
import retrofit2.http.GET

/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-05-23 00:16.
 * @Description : 生活很难，但总有人在爱你。
 */
interface SentenceService {
    @GET("https://api.xygeng.cn/one")
    fun getSentence(): Call<SentenceBean>
}