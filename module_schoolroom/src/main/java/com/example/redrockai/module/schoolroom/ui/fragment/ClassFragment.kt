package com.example.redrockai.module.schoolroom.ui.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.redrockai.lib.utils.formatDateStringWithLocalDate
import com.example.redrockai.module.schoolroom.adapter.RelatedIntroduceAdapter
import com.example.redrockai.module.schoolroom.bean.CateGoryBean
import com.example.redrockai.module.schoolroom.databinding.FragmentClassBinding
import com.example.redrockai.module.schoolroom.helper.room.bean.HistoryRecord
import com.example.redrockai.module.schoolroom.helper.room.dao.HistoryRecordDao
import com.example.redrockai.module.schoolroom.helper.room.db.AppDatabase
import com.example.redrockai.module.schoolroom.ui.activity.HistoryRecordActivity
import com.example.redrockai.module.schoolroom.viewModel.CateGoryViewModel
import com.example.redrockai.module.schoolroom.viewModel.ImageViewModel
import com.example.redrockai.module.schoolroom.viewModel.IntroduceViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ClassFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private var _mBinding: FragmentClassBinding? = null
    private val mBinding: FragmentClassBinding
        get() = _mBinding!!
    private val newFollowViewModel by lazy {
        ViewModelProvider(this)[CateGoryViewModel::class.java]
    }
    private val introduceViewModel by lazy {
        ViewModelProvider(this)[IntroduceViewModel::class.java]
    }
    private val imageViewModel by lazy {
        ViewModelProvider(this)[ImageViewModel::class.java]
    }
    private lateinit var adapter: RelatedIntroduceAdapter
    private lateinit var list: List<CateGoryBean.CateGoryBeanItem>

    //历史消息room的dao接口
    private lateinit var historyRecordDao: HistoryRecordDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentClassBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniView()
        iniTabLayout()
        initSchoolRoomRv()
        initClickListener()
        initImage()


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initImage() {
        imageViewModel.getImage()
        imageViewModel.imageDate.observe(viewLifecycleOwner) {
            Glide.with(this).load(it.data.urls.get(11)).into(mBinding.ivBing)
            mBinding.tvDetail.text = it.data.title


            mBinding.tvData.text = formatDateStringWithLocalDate(it.data.time)
        }
    }


    /**
     * 初始化一些操作
     */
    private fun iniView() {
        newFollowViewModel.getCateGory()
        historyRecordDao = AppDatabase.getDatabaseSingleton().historyRecordDao()

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun initSchoolRoomRv() {
        adapter = RelatedIntroduceAdapter().apply {
            setOnClassItemClickListener {
                // 生成一条记录
                val record = HistoryRecord(
                    newsId = it.data.content.data.id,
                    title = it.data.content.data.title,
                    timestamp = System.currentTimeMillis(),
                    url = it.data.content.data.playUrl,
                    description = it.data.content.data.description,
                    coverDetail = it.data.content.data.cover.detail
                )
                GlobalScope.launch {
                    historyRecordDao.insertOrUpdate(record)
                }

                startActivity(Intent(requireContext(), HistoryRecordActivity::class.java))


            }
        }
        newFollowViewModel.cateGoryData.observe(viewLifecycleOwner) {
            val categories = it
            list = categories
            categories.forEach { category ->
                // 在这里处理每个元素，
                val tab = tabLayout.newTab()
                tab.text = category.name
                tabLayout.addTab(tab)
            }
        }
        introduceViewModel.relatedCategoryData.observe(viewLifecycleOwner) {
            adapter.submitList(it.itemList)
        }
        mBinding.recyclerviewIntroduce.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        mBinding.recyclerviewIntroduce.adapter = adapter
    }

    private fun iniTabLayout() {
        mBinding.apply {
            tabLayout = classTablayout
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val selectedTabIndex = tab?.position ?: -1
                    introduceViewModel.getRelatedCategoryData(list[selectedTabIndex].tagId)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // 当Tab取消选中时的处理
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // 当Tab重新选中时的处理
                }
            })
        }
    }

    private fun initClickListener() {
        mBinding.apply {
            coursePast.setOnClickListener {
                startActivity(Intent(requireContext(), HistoryRecordActivity::class.java))
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }


}