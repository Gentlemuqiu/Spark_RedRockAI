package com.examole.redrockai.module_mine.ui

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.examole.redrockai.module_mine.SignInActivity
import com.examole.redrockai.module_mine.MineViewModel
import com.examole.redrockai.module_mine.databinding.FragmentMineBinding
import com.example.redrockai.lib.utils.StudyTimeUtils.convertTimestampToMinutes
import com.example.redrockai.lib.utils.room.dao.HistoryRecordDao
import com.example.redrockai.lib.utils.room.db.AppDatabase
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch
import java.util.Calendar


class MineFragment : Fragment() {
    //获取数据库代理抽象类实例
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.upData()
        initView()
        initObserve()
        initLineChart()
        mBinding.ivMineLogo.setOnClickListener {
            startActivity(Intent(requireActivity(), SignInActivity::class.java))
        }
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initLineChart() {
        // 获取并显示应用使用时间折线图
        val usageStatsList = getUsageStats(requireContext())
        displayChart(usageStatsList)

    }


    // 获取当天每小时的应用使用时间
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getUsageStats(context: Context): List<UsageStats> {
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()
        return usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startTime,
            endTime
        )
    }

    // 处理使用数据，按小时分组统计
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getHourlyUsage(usageStatsList: List<UsageStats>): Map<Int, Long> {
        val hourlyUsage = mutableMapOf<Int, Long>()

        for (usageStats in usageStatsList) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = usageStats.firstTimeStamp

            val startHour = calendar.get(Calendar.HOUR_OF_DAY)
            val usageTime = usageStats.totalTimeInForeground

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                hourlyUsage[startHour] = hourlyUsage.getOrDefault(startHour, 0L) + usageTime
            }
        }

        return hourlyUsage
    }

    // 显示应用使用时间折线图
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun displayChart(usageStatsList: List<UsageStats>) {
        val hourlyUsage = getHourlyUsage(usageStatsList)
        Log.d("wefawfwe", "测试数据${hourlyUsage}")

        val entries = ArrayList<Entry>()

        for (hour in 0..23) {
            val usageTime = hourlyUsage[hour]?.div(60000)?.toFloat() ?: 0f // 转为分钟
            entries.add(Entry(hour.toFloat(), usageTime))
        }
        Log.d("fwefwefa", "测试数据${entries}")

        val lineChart: LineChart = mBinding.lineChart
        val dataSet = LineDataSet(entries, "每日使用时间/分钟")
        val lineData = LineData(dataSet)
        lineChart.data = lineData

        // 配置X轴
        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 2f // 每个小时一个刻度
        xAxis.labelCount = 24
        xAxis.setDrawGridLines(false)

        // 配置Y轴
        val yAxisLeft: YAxis = lineChart.axisLeft
        yAxisLeft.granularity = 1f // 每分钟一个刻度
        yAxisLeft.labelCount = 10
        yAxisLeft.axisMinimum = 0f // 从0开始
        yAxisLeft.setDrawGridLines(false)

        // 关闭右边的Y轴
        val yAxisRight: YAxis = lineChart.axisRight
        yAxisRight.isEnabled = false
        // 隐藏描述标签
        lineChart.description.isEnabled = false
        lineChart.invalidate()

    }


    override fun onResume() {
        super.onResume()
        mViewModel.upData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initLineChart()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }


}