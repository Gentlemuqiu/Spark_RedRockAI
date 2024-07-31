package com.example.module.life.net

/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-06-02 20:31.
 * @Description : 生活很难，但总有人在爱你。
 */
import com.example.module.life.bean.ArticleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("article/list/{page}/json")
    suspend fun getArticles(@Path("page") page: Int): ArticleResponse
    @POST("article/query/{page}/json")
    suspend fun searchArticles(
        @Path("page") page: Int,
        @Query("k") keyword: String
    ): Response<ArticleResponse>
}