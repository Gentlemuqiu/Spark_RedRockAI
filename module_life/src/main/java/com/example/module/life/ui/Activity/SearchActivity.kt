package com.example.module.life.ui.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import com.example.module.life.Adapter.SearchHistoryAdapter
import com.example.module.life.R
import com.example.module.life.ViewModel.SearchActivityViewModel
import com.example.module.life.databinding.ActivitySearchBinding
import com.example.module.life.ui.dialog.ReminderDialog
import com.example.redrockai.lib.utils.BaseActivity
import com.example.redrockai.lib.utils.gsonSaveToSp
import com.example.redrockai.lib.utils.objectFromSp
import com.example.redrockai.lib.utils.view.gone
import com.example.redrockai.lib.utils.view.visible
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.tabs.TabLayout

class SearchActivity : BaseActivity() {
    private val mBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }
    private val mViewModel by viewModels<SearchActivityViewModel>()
    private lateinit var mAdapter: SearchHistoryAdapter
    private val historyList by lazy { LinkedHashSet<String>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initBack()
        initHistory()
        initEditText()
        initSearch()
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
        mViewModel.setSearchContent(content)
    }

    private fun saveHistoryList() {
        gsonSaveToSp(historyList, this.javaClass.name)
    }

    private fun showResult() {
        mBinding.searchActivitySearchLinearHistory.gone()
    }

    private fun showHistory() {
        mBinding.searchActivitySearchLinearHistory.visible()
    }





    }

