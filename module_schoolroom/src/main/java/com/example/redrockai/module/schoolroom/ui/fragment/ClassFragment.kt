package com.example.redrockai.module.schoolroom.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redrockai.module.schoolroom.databinding.FragmentClassBinding
import com.example.redrockai.module.schoolroom.ui.fragment.Bean.CateGoryBean
import com.example.redrockai.module.schoolroom.ui.fragment.adapter.RelatedIntroduceAdapter
import com.example.redrockai.module.schoolroom.ui.fragment.viewModel.CateGoryViewModel
import com.example.redrockai.module.schoolroom.ui.fragment.viewModel.IntroduceViewModel
import com.google.android.material.tabs.TabLayout


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
    private lateinit var adapter: RelatedIntroduceAdapter
    private lateinit var list: List<CateGoryBean.CateGoryBeanItem>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentClassBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newFollowViewModel.getCateGory()
        iniTabLayout()
        adapter = RelatedIntroduceAdapter()
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
        mBinding.recyclerviewIntroduce.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL, false)
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


    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }


}