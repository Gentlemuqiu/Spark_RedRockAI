package com.example.redrockai.module.schoolroom.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.redrockai.lib.utils.adapter.FragmentVpAdapter
import com.example.redrockai.module.schoolroom.R
import com.example.redrockai.module.schoolroom.databinding.FragmentClassBinding
import com.example.redrockai.module.schoolroom.ui.fragment.schooltabs.CommunityFragment
import com.example.redrockai.module.schoolroom.ui.fragment.schooltabs.DailyFragment
import com.example.redrockai.module.schoolroom.ui.fragment.schooltabs.FindFragment
import com.example.redrockai.module.schoolroom.ui.fragment.schooltabs.RecommendFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class ClassFragment : Fragment() {


    private var _mBinding: FragmentClassBinding? = null
    private val mBinding: FragmentClassBinding
        get() = _mBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentClassBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniTabLayout()
    }

    private fun iniTabLayout() {
        mBinding.apply {
            classViewpager2.adapter = FragmentVpAdapter(requireActivity())
                .add { FindFragment() }
                .add { RecommendFragment() }
                .add { DailyFragment() }
                .add { CommunityFragment() }
            TabLayoutMediator(classTablayout, classViewpager2) { tab, position ->
                when (position) {
                    0 -> tab.text = "发现"
                    1 -> tab.text = "推荐"
                    2 -> tab.text = "日报"
                    else -> tab.text = "社区"
                }
            }.attach()
            //让初始化的第一个先变色
            (classTablayout.getChildAt(0) as ViewGroup).getChildAt(0)
                .setBackgroundResource(R.drawable.ic_tab_shape_selected)
            //设置其他icon颜色
            for (i in 1 until classTablayout.tabCount) {
                val tab = (classTablayout.getChildAt(0) as ViewGroup).getChildAt(i)
                tab.setBackgroundResource(R.drawable.ic_tab_shape)
            }
            //设置间距
            for (i in 0 until classTablayout.tabCount) {
                val tab = (classTablayout.getChildAt(0) as ViewGroup).getChildAt(i)
                val params = tab.layoutParams as ViewGroup.MarginLayoutParams
                params.setMargins(10, params.topMargin, 10, params.bottomMargin)
                tab.requestLayout()
            }
            /**
             * 实现滑动到特定区域和没有的颜色区分
             */
            classTablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    tab.view.setBackgroundResource(R.drawable.ic_tab_shape_selected)
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    tab.view.setBackgroundResource(R.drawable.ic_tab_shape)
                }

                override fun onTabReselected(tab: TabLayout.Tab) {}
            })

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }


}