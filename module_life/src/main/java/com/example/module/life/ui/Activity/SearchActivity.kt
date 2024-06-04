package com.example.module.life.ui.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module.life.Adapter.LoadingStateAdapter
import com.example.module.life.Adapter.SearchAdapter
import com.example.module.life.Adapter.SearchHistoryAdapter
import com.example.module.life.R
import com.example.module.life.ViewModel.SearchViewModel
import com.example.module.life.ViewModel.SearchViewModelFactory
import com.example.module.life.databinding.ActivitySearchBinding
import com.example.module.life.net.ApiService
import com.example.module.life.ui.dialog.ReminderDialog
import com.example.redrockai.lib.utils.BaseActivity
import com.example.redrockai.lib.utils.gsonSaveToSp
import com.example.redrockai.lib.utils.objectFromSp
import com.example.redrockai.lib.utils.view.gone
import com.example.redrockai.lib.utils.view.visible
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : BaseActivity() {
    private val mBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }
    private lateinit var mAdapter: SearchHistoryAdapter
    private val historyList by lazy { LinkedHashSet<String>() }
    private val searchViewModel by lazy {
        ViewModelProvider(
            this,
            SearchViewModelFactory(apiService)
        )[SearchViewModel::class.java]
    }
    private lateinit var searchAdapter: SearchAdapter


    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initBack()
        initHistory()
        initEditText()
        initSearch()
        initRV()
    }

    private fun initRV() {
        searchAdapter = SearchAdapter()
        val recyclerView: RecyclerView =mBinding.searchRv
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = searchAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter { searchAdapter.retry() }
        )

        searchViewModel.articles.observe(this) {
            searchAdapter.submitData(lifecycle, it)
        }


    }

    private fun initBack() {
        mBinding.searchActivitySearchImgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val backCallBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (mBinding.searchActivitySearchLinearHistory.visibility == View.GONE) {
                    showHistory()
                }else if (mBinding.searchActivitySearchTvComplete.visibility == View.VISIBLE) {
                    deleteComplete()
                } else {
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, backCallBack)
    }

    private fun initEditText() {
        mBinding.searchActivitySearchEtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun initHistory() {
        initAdapter()
        initRv()
        initData()
        initClearClick()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initClearClick() {
        with(mBinding) {
            searchActivitySearchImgGarbage.setOnClickListener {
                // 对应状态的切换
                searchActivitySearchTvClearAll.visible()
                searchActivitySearchTvComplete.visible()
                tvOr.visible()
                searchActivitySearchImgGarbage.gone()
                mAdapter.isDeleteStatus = true
                mAdapter.notifyDataSetChanged()
            }
            searchActivitySearchTvClearAll.setOnClickListener {
                val revokeReminderDialog = ReminderDialog(this@SearchActivity)
                revokeReminderDialog.setConfirmSelected {
                    historyList.clear()
                    mAdapter.submitList(historyList.toList())
                    it.cancel()
                }.show()

            }
            searchActivitySearchTvComplete.setOnClickListener {
                deleteComplete()
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteComplete() {
        if (mBinding.searchActivitySearchTvComplete.visibility == View.VISIBLE){
            // 对应状态的切换
            with(mBinding) {
                searchActivitySearchTvClearAll.gone()
                searchActivitySearchTvComplete.gone()
                tvOr.gone()
                searchActivitySearchImgGarbage.visible()
                mAdapter.isDeleteStatus = false
                mAdapter.notifyDataSetChanged()
                saveHistoryList()
            }
        }
    }

    private fun initData() {
        val lastHistory = objectFromSp<LinkedHashSet<String>>(this.javaClass.name)
        if (!lastHistory.isNullOrEmpty()) {
            historyList.addAll(lastHistory)
            setHistoryList()
        }
    }

    private fun setHistoryList() {
        mAdapter.submitList(historyList.toList())
    }


    private fun initRv() {
        val manager = FlexboxLayoutManager(this)
        manager.apply {
            // 附轴对齐方式 我这里让他沿着轴从头到尾排
            alignItems = AlignItems.FLEX_START
        }
        mBinding.searchActivitySearchRvHistory.layoutManager = manager
        mBinding.searchActivitySearchRvHistory.adapter = mAdapter
    }

    private fun initAdapter() {
        mAdapter = SearchHistoryAdapter().apply {
            setOnClickItem { content ->
                mBinding.searchActivitySearchEtSearch.setText(content)
                mBinding.searchActivitySearchEtSearch.setSelection(content.length)
                sendSearchEvent(content)
            }
            setOnClickCha { s->
                historyList.remove(s)
                mAdapter.submitList(historyList.toList())
            }
        }
    }

    private fun initSearch() {
        mBinding.searchActivitySearchTvSearch.setOnClickListener {
            doSearch()
        }
    }

    private fun doSearch() {
        val text = mBinding.searchActivitySearchEtSearch.text.toString()
        sendSearchEvent(text)
    }

    private fun sendSearchEvent(content: String) {
        deleteComplete()
        showResult()
        if (content.isNotEmpty()) {
            historyList.add(content)
            setHistoryList()
        }
        saveHistoryList()
        searchViewModel.searchArticles(content)
    }

    private fun saveHistoryList() {
        gsonSaveToSp(historyList, this.javaClass.name)
    }

    private fun showResult() {
        mBinding.searchRv.visible()
        mBinding.searchActivitySearchLinearHistory.gone()
    }

    private fun showHistory() {
        mBinding.searchActivitySearchLinearHistory.visible()
        mBinding.searchRv.gone()
    }





    }

