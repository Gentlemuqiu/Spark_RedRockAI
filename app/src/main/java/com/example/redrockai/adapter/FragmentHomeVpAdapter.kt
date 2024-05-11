package com.example.redrockai.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/5/11 17:01
 *  version : 1.0
 *  description :
 *  saying : 这世界天才那么多，也不缺我一个
 */
open class FragmentHomeVpAdapter(
    fragmentActivity: FragmentActivity,
) :
    FragmentStateAdapter(fragmentActivity) {

    private val mFragments = arrayListOf<() -> Fragment>()

    open fun add(fragment: () -> Fragment): FragmentHomeVpAdapter {
        mFragments.add(fragment)
        return this
    }

    override fun getItemCount(): Int {
        return mFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragments[position].invoke()
    }

}