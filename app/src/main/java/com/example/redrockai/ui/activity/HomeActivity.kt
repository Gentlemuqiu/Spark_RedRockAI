package com.example.redrockai.ui.activity

import android.os.Bundle
import androidx.core.view.WindowCompat
import com.example.module.life.ui.Fragment.LifeFragment
import com.example.redrockai.R
import com.example.redrockai.adapter.FragmentHomeVpAdapter
import com.example.redrockai.databinding.ActivityHomeBinding
import com.example.redrockai.lib.utils.BaseActivity
import com.example.redrockai.module.schoolroom.ui.fragment.ClassFragment
import com.examole.redrockai.module_mine.ui.fragment.MineFragment
import com.example.redrockai.module.message.MessageFragment


class HomeActivity : BaseActivity() {

    private val mBinding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initWindow()
        initBottomNavigation()
    }

    private fun initWindow() {
       //状态栏不沉浸
        WindowCompat.setDecorFitsSystemWindows(window, true)
        /* 隐藏导航栏
        *getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        * */
    }


    private fun initBottomNavigation() {
        mBinding.apply {
            vp2.adapter = FragmentHomeVpAdapter(this@HomeActivity).apply {
                add { ClassFragment() }
                add { LifeFragment() }
                add { MessageFragment() }
                add { MineFragment() }
            }
            vp2.isUserInputEnabled = false
            bottomNavigationView.itemIconTintList = null
            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.bottom_class -> mBinding.vp2.currentItem = 0
                    R.id.bottom_life -> mBinding.vp2.currentItem = 1
                    R.id.bottom_message -> mBinding.vp2.currentItem = 2
                    R.id.bottom_mine -> mBinding.vp2.currentItem = 3
                }
                return@setOnItemSelectedListener true
            }

        }
    }


}