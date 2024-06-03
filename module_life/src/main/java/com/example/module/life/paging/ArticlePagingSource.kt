package com.example.module.life.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.module.life.bean.ArticleResponse
import com.example.module.life.net.ApiService
import retrofit2.HttpException
import java.io.IOException

/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-06-02 20:41.
 * @Description : 生活很难，但总有人在爱你。
 */
class ArticlePagingSource(
    private val apiService: ApiService
) : PagingSource<Int, ArticleResponse.Data.Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleResponse.Data.Article> {
        val page = params.key ?: 0
        return try {
            val response = apiService.getArticles(page)
            val articles = response.data.datas

            LoadResult.Page(
                data = articles,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (articles.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArticleResponse.Data.Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}