package com.example.module.life.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.module.life.bean.ArticleResponse
import com.example.module.life.net.ApiService

/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-06-03 22:36.
 * @Description : 生活很难，但总有人在爱你。
 */
class SearchPagingSource(
    private val apiService: ApiService,
    private val keyword: String
) : PagingSource<Int, ArticleResponse.Data.Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleResponse.Data.Article> {
        val page = params.key ?: 0
        return try {
            val response = apiService.searchArticles(page, keyword)
            val articles = response.body()?.data?.datas ?: emptyList()
            val nextKey = if (articles.isEmpty()) null else page + 1
            LoadResult.Page(
                data = articles,
                prevKey = if (page == 0) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
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
