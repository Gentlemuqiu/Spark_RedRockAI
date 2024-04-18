package com.example.redrockai.lib.utils.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-04-18 19:50.
 * @Description : 生活很难，但总有人在爱你。
 */
//主要用于vp的使用
open class FragmentVpAdapter private constructor(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    //用于在activity中使用viewPager2，传入this即可
    constructor(activity: FragmentActivity) : this(
        activity.supportFragmentManager,
        activity.lifecycle
    )

    //用于在fragment使用Viewpager2，传入this应该也就可以了
    constructor(fragment: Fragment) : this(fragment.childFragmentManager, fragment.lifecycle)

    //这个是一个lambda表达式的list，并没有创建fragment，维护的是待创建的fragment对象
    protected val mFragments = arrayListOf<() -> Fragment>()

   //传入一段lambda表达式，然后储存到list中
    open fun add(fragment: () -> Fragment): FragmentVpAdapter {
        mFragments.add(fragment)
        return this
    }
    //传入fragment的类::，这个要注意fragment中要有newInstance静态方法
    open fun add(fragment: Class<out Fragment>): FragmentVpAdapter {
        // 官方源码中在恢复 Fragment 时就是调用的这个反射方法，该方法应该不是很耗性能 :)
        mFragments.add { fragment.newInstance() }
        return this
    }

    override fun getItemCount(): Int = mFragments.size
   ////6 开始创建fragment，调用list指定位置的fragmet的lambda表达式的invoke方法，也就会调用lambda表达式创建fragment，也就是获得Fragment实例
    override fun createFragment(position: Int): Fragment = mFragments[position].invoke()
}
