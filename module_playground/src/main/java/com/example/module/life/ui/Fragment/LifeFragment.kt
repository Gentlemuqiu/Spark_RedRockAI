package com.example.module.life.ui.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module.life.Adapter.ArticleAdapter
import com.example.module.life.Adapter.LoadingStateAdapter
import com.example.module.life.ViewModel.ArticleViewModel
import com.example.module.life.ViewModel.ArticleViewModelFactory
import com.example.module.life.databinding.FragmentLifeBinding
import com.example.module.life.net.ApiService
import com.example.module.life.ui.Activity.SearchActivity
import com.example.module.life.ui.Activity.WebActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LifeFragment : Fragment() {

    private var _mBinding: FragmentLifeBinding? = null
    private val mBinding: FragmentLifeBinding
        get() = _mBinding!!
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var loadingStateAdapter: LoadingStateAdapter


    private val articleViewModel by lazy {
        ViewModelProvider(
            this,
            ArticleViewModelFactory(apiService)
        )[ArticleViewModel::class.java]
    }


    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentLifeBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = mBinding.lifeRv
        mBinding.lifeRv.layoutManager = LinearLayoutManager(this.context)
        articleAdapter = ArticleAdapter()
        loadingStateAdapter = LoadingStateAdapter { articleAdapter.retry() }
        val concatAdapter = articleAdapter.withLoadStateFooter(footer = loadingStateAdapter)

        recyclerView.adapter = concatAdapter
        mBinding.swipeRefreshLayout.setOnRefreshListener {
            articleViewModel.refreshArticles()
        }
        lifecycleScope.launch {
            articleViewModel.articles.collectLatest { pagingData ->
                mBinding.swipeRefreshLayout.isRefreshing = false
                articleAdapter.submitData(pagingData)
            }
        }
        articleAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                mBinding.swipeRefreshLayout.isRefreshing = true
            } else {
                mBinding.swipeRefreshLayout.isRefreshing = false
            }
        }
        mBinding.lifeSearch.setOnClickListener{
            val intent = Intent(context,SearchActivity::class.java)
            context?.startActivity(intent)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }


}