package com.example.module.life.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.module.life.net.ApiService
import com.example.module.life.paging.SearchPagingSource

/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-06-03 22:37.
 * @Description : 生活很难，但总有人在爱你。
 */
class SearchViewModel(private val apiService: ApiService) : ViewModel() {
    private val keyword = MutableLiveData<String>()

    val articles = keyword.switchMap { query ->
        Pager(PagingConfig(pageSize = 20)) {
            SearchPagingSource(apiService, query)
        }.liveData.cachedIn(viewModelScope)
    }

    fun searchArticles(newKeyword: String) {
        keyword.value = newKeyword
    }
}

