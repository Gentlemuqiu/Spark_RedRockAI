package com.example.module.life.net

/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-06-02 20:31.
 * @Description : 生活很难，但总有人在爱你。
 */
import com.example.module.life.bean.ArticleResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("article/list/{page}/json")
    suspend fun getArticles(@Path("page") page: Int): ArticleResponse
}