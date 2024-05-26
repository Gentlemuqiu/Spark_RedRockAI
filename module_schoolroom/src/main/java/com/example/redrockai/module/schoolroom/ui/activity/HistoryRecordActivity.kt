package com.example.redrockai.module.schoolroom.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.example.redrockai.lib.utils.BaseActivity
import com.example.redrockai.module.schoolroom.adapter.PastCourseRvAdapter
import com.example.redrockai.module.schoolroom.databinding.ActivityHistoryBinding
import com.example.redrockai.module.schoolroom.viewModel.HistoryRecordViewModel
import kotlinx.coroutines.launch

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/5/20 20:03
 *  version : 1.0
 *  description :历史记录activity
 *  saying : 这世界天才那么多，也不缺我一个
 */
class HistoryRecordActivity : BaseActivity() {


    private val mBinding: ActivityHistoryBinding by lazy {
        ActivityHistoryBinding.inflate(layoutInflater)
    }


    private val mViewModel: HistoryRecordViewModel by viewModels()
    private val mAdapter: PastCourseRvAdapter by lazy { PastCourseRvAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initRv()
        initListener()


    }


    /**
     * 展示历史数据列表
     */
    private fun initRv() {

        mBinding.apply {
            pastRv.adapter = mAdapter.apply {
                setOnClassItemClickListener {
                    ARouter.getInstance().build("/play/PlayActivity/")
                        .withString("playUrl", it.playerUrl)
                        .withString("title", it.title)
                        .withString("description", it.description)
                        .withString("category", it.category)
                        .withInt("shareCount", it.shareCount)
                        .withInt("likeCount", it.likeCount)
                        .withInt("commentCount", it.commentCount)
                        .withInt("id", it.newsId)
                        .navigation(this@HistoryRecordActivity)
                }

            }
            pastRv.layoutManager = LinearLayoutManager(this@HistoryRecordActivity)
        }

        lifecycleScope.launch {
            mViewModel.historyRecords.collect {
                mAdapter.submitList(it)
            }
        }
    }

    private fun initListener() {

        mBinding.apply {
            historyBack.setOnClickListener { finish() }
            historySearchView.apply {
                setOnQueryTextListener(object :
                    androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        if (query.isNotEmpty()) {
                            //搜索操作
                            mViewModel.getSearchData(query)
                            historySearchView.clearFocus()
                        }
                        return false
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        return false
                    }
                })
            }

            historySearchCard.setOnClickListener {
                historySearchView.isIconified = false
            }


        }

    }


}