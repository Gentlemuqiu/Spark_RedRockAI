package com.examole.redrockai.module_mine.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.examole.redrockai.module_mine.MineViewModel
import com.examole.redrockai.module_mine.databinding.FragmentMineBinding
import com.example.redrockai.lib.utils.StudyTimeUtils.convertTimestampToMinutes
import com.example.redrockai.lib.utils.room.dao.HistoryRecordDao
import com.example.redrockai.lib.utils.room.db.AppDatabase
import kotlinx.coroutines.launch


class MineFragment : Fragment() {
    //历史消息room的dao接口
    private lateinit var historyRecordDao: HistoryRecordDao

    private val mViewModel: MineViewModel by viewModels()

    private var _mBinding: FragmentMineBinding? = null
    private val mBinding get() = _mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentMineBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.upData()
        initView()
        initObserve()
    }


    private fun initView() {
        historyRecordDao = AppDatabase.getDatabaseSingleton().historyRecordDao()
    }

    //更新UI
    private fun initObserve() {
        lifecycleScope.launch {
            mViewModel.mineData.collect {
                mBinding.apply {
                    tvKc.text = it.studyCourseNum.toString().plus("个")
                    //学习时间
                    tvSj.text = convertTimestampToMinutes(it.studyTimeAll!!).toString().plus("分钟")


                }

            }
        }
    }




    override fun onResume() {
        super.onResume()
        mViewModel.upData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }


}