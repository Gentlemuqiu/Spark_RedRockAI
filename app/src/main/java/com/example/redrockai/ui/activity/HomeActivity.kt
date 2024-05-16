package com.example.redrockai.ui.activity

import android.os.Bundle
import com.example.redrockai.R
import com.example.redrockai.adapter.FragmentHomeVpAdapter
import com.example.redrockai.databinding.ActivityHomeBinding
import com.example.redrockai.lib.utils.BaseActivity
import com.example.redrockai.module.schoolroom.ui.fragment.ClassFragment
import com.example.redrockai.ui.fragment.LifeFragment
import com.example.redrockai.ui.fragment.MessageFragment
import com.example.redrockai.ui.fragment.MineFragment

class HomeActivity : BaseActivity() {

    private val mBinding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initBottomNavigation()
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
                    R.id.bottom_message -> mBinding.vp2.currentItem = 1
                    R.id.bottom_mine -> mBinding.vp2.currentItem = 1
                }
                return@setOnItemSelectedListener true
            }

        }
    }


}