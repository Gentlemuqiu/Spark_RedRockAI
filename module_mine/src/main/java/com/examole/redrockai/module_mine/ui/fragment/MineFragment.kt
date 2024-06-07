package com.examole.redrockai.module_mine.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.examole.redrockai.module_mine.databinding.FragmentMineBinding
import com.examole.redrockai.module_mine.ui.activity.SignInActivity
import com.examole.redrockai.module_mine.viewmodel.MineViewModel
import com.example.redrockai.lib.utils.BaseApp
import com.example.redrockai.lib.utils.StudyTimeUtils
import com.example.redrockai.lib.utils.StudyTimeUtils.convertTimestampToMinutes
import com.example.redrockai.lib.utils.getStringList
import com.example.redrockai.lib.utils.room.dao.HistoryRecordDao
import com.example.redrockai.lib.utils.room.db.AppDatabase
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MineFragment : Fragment() {
    //获取数据库代理抽象类实例
    private lateinit var historyRecordDao: HistoryRecordDao

    private val mViewModel: MineViewModel by viewModels()

    private var _mBinding: FragmentMineBinding? = null
    private val mBinding get() = _mBinding!!
    private val sharedPreferences =
        BaseApp.getAppContext().getSharedPreferences("SignInPrefs", Context.MODE_PRIVATE)


    private val REQUEST_CODE_PICK_IMAGE = 1
    private val REQUEST_CODE_CROP_IMAGE = 2
    private val PREFS_NAME = "MineFragmentPrefs"
    private val PREF_IMAGE_URI = "imageUri"



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
        displayBarChart()
        mBinding.ivMineLogo.setOnClickListener {
            openGallery()
        }
        loadSavedImageLogo()
        mBinding.tvDk.setOnClickListener {
            startActivity(Intent(requireActivity(), SignInActivity::class.java))
        }
        mBinding.textView.setOnClickListener {
            startActivity(Intent(requireActivity(), SignInActivity::class.java))
        }
    }


    private fun loadSavedImageLogo() {
        //加载已经有的
        val prefs = requireActivity().getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE)
        val savedImageUri = prefs.getString(PREF_IMAGE_URI, null)
        savedImageUri?.let {
            mBinding.ivMineLogo.setImageURI(Uri.parse(it))
        }
    }


    //相当于跳转到了UCropActivity
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    data?.data?.let { uri ->
                        startCrop(uri)
                    }
                }

                UCrop.REQUEST_CROP -> {
                    data?.let {
                        val resultUri = UCrop.getOutput(it)
                        resultUri?.let { uri ->
                            mBinding.ivMineLogo.setImageURI(uri)
                            saveImageUri(uri)
                        }
                    }
                }
            }
        }
    }

    private fun startCrop(uri: Uri) {
        //必须要设置为System.currentTimeMillis()保证唯一性，否则第二次裁剪会无效
        val destinationUri =
            Uri.fromFile(File(requireContext().cacheDir, "${System.currentTimeMillis()}.jpg"))
        val options = UCrop.Options().apply {
            setFreeStyleCropEnabled(false)
            setCompressionFormat(Bitmap.CompressFormat.PNG)
        }
        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withOptions(options)
            .start(requireContext(), this)
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun saveImageUri(uri: Uri) {
        val prefs = requireActivity().getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE).edit()
        prefs.putString(PREF_IMAGE_URI, uri.toString())
        prefs.apply()
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


    // 显示应用使用时间柱状图
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun displayBarChart() {
        val daysUsageTime = StudyTimeUtils.getDaysUsageTime()
        val entries = ArrayList<BarEntry>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sortedDates = daysUsageTime.keys.sorted().map { dateFormat.parse(it) }

        sortedDates.forEachIndexed { index, date ->
            val calendar = Calendar.getInstance()
            calendar.time = date
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH).toFloat()
            val usageTime =
                daysUsageTime[dateFormat.format(date)]?.div(60000)?.toFloat() ?: 0f // 转为分钟
            entries.add(BarEntry(dayOfMonth - 1, usageTime))
        }

        val barChart: BarChart = mBinding.barChart
        val dataSet = BarDataSet(entries, "每日使用时间/分钟").apply {

        }
        val barData = BarData(dataSet).apply {
            if (sortedDates.size < 3) {
                barWidth = 0.2f // 如果数据点小于 3 个,设置柱状图宽度为 0.3
            }
        }
        barChart.data = barData

        // 配置X轴
        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = object : ValueFormatter() {
            @SuppressLint("ConstantLocale")
            private val monthFormat = SimpleDateFormat("M月d号", Locale.getDefault())
            override fun getFormattedValue(value: Float): String {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.DAY_OF_MONTH, value.toInt() + 1)
                return monthFormat.format(calendar.time)
            }
        }
        xAxis.granularity = 1f
        xAxis.labelCount = sortedDates.size
        xAxis.setDrawGridLines(true)


        // 配置Y轴
        val yAxisLeft: YAxis = barChart.axisLeft
        yAxisLeft.granularity = 1f
        yAxisLeft.labelCount = 10
        yAxisLeft.axisMinimum = 0f
        yAxisLeft.setDrawGridLines(true)

        // 关闭右边的Y轴
        val yAxisRight: YAxis = barChart.axisRight
        yAxisRight.isEnabled = false

        // 隐藏描述标签
        barChart.description.isEnabled = false
        barChart.invalidate()
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        mViewModel.upData()
        displayBarChart()
        val dateList = sharedPreferences.getStringList("signed_dates")
        mBinding.tvDk.text = dateList.size.toString().plus("次")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }


}