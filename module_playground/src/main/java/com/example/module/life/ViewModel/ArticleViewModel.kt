package com.example.module.life.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.module.life.bean.ArticleResponse
import com.example.module.life.net.ApiService
import com.example.module.life.paging.ArticlePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-06-02 20:45.
 * @Description : 生活很难，但总有人在爱你。
 */
class ArticleViewModel(private val apiService: ApiService) : ViewModel() {

    private val _articles = MutableStateFlow<PagingData<ArticleResponse.Data.Article>>(PagingData.empty())
    val articles: Flow<PagingData<ArticleResponse.Data.Article>> = _articles

    init {
        getArticles()
    }

    fun getArticles() {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                pagingSourceFactory = { ArticlePagingSource(apiService) }
            ).flow.cachedIn(viewModelScope).collectLatest {
                _articles.value = it
            }
        }
    }

    fun refreshArticles() {
        getArticles()
    }
}