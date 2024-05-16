package com.example.redrockai.module.schoolroom.ui.fragment.schooltabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.redrockai.module.schoolroom.databinding.FragmentTabDailyBinding

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/5/16 15:31
 *  version : 1.0
 *  description :
 *  saying : 这世界天才那么多，也不缺我一个
 */
class DailyFragment : Fragment() {

    private var _mBinding: FragmentTabDailyBinding? = null
    private val mBinding get() = _mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentTabDailyBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }


}