package com.examole.redrockai.module_mine.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.launcher.ARouter
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
import com.iflytek.sparkchain.core.LLM
import com.iflytek.sparkchain.core.LLMCallbacks
import com.iflytek.sparkchain.core.LLMConfig
import com.iflytek.sparkchain.core.LLMError
import com.iflytek.sparkchain.core.LLMEvent
import com.iflytek.sparkchain.core.LLMResult
import com.iflytek.sparkchain.core.Memory
import com.iflytek.sparkchain.core.SparkChain
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Locale


class MineFragment : Fragment() {
    //获取数据库代理抽象类实例
    private lateinit var historyRecordDao: HistoryRecordDao

    private val mViewModel: MineViewModel by viewModels()

    private var _mBinding: FragmentMineBinding? = null
    private val mBinding get() = _mBinding!!
    private val sharedPreferences =
        BaseApp.getAppContext().getSharedPreferences("SignInPrefs", Context.MODE_PRIVATE)

    private var studyTime: String? = null


    private val TAG = "AEE"
    private var sessionFinished = true
    private lateinit var llm: LLM
    private val accumulatedContent = StringBuilder()
    private var temporaryMessageIndex: Int? = null


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
        //建议初始化
        setLLMConfig()
        mBinding.apply {
            textView2.setOnClickListener {
                intentToHistory()
            }
            tvKc.setOnClickListener {
                intentToHistory()
            }
        }
    }

    private fun intentToHistory() {
        ARouter.getInstance().build("/schoolroom/historyactivity/").navigation()
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
                    studyTime = convertTimestampToMinutes(it.studyTimeAll!!).toString().plus("分钟")

                }
            }
        }
    }

    private var llmCallbacks: LLMCallbacks = object : LLMCallbacks {
        override fun onLLMResult(llmResult: LLMResult, usrContext: Any?) {
            val content: String = llmResult.content
            val status: Int = llmResult.status

            activity?.runOnUiThread {
                accumulatedContent.append(content)
                mBinding.chatAdvice.append(content)
                sessionFinished = true

            }
        }

        override fun onLLMEvent(event: LLMEvent, usrContext: Any?) {
            Log.d(TAG, "onLLMEvent\n")
        }

        override fun onLLMError(error: LLMError, usrContext: Any?) {
            Log.d(TAG, "onLLMError\n")
            sessionFinished = true
        }
    }


    private fun setLLMConfig() {
        val llmConfig: LLMConfig = LLMConfig.builder()
        llmConfig.domain("generalv3.5")
        llmConfig.url("ws(s)://spark-api.xf-yun.com/v3.5/chat")
        llmConfig.maxToken(8192)
        val window_memory: Memory = Memory.windowMemory(5)
        llm = LLM(llmConfig, window_memory)
        llm.registerLLMCallbacks(llmCallbacks)
        startChat()
    }

    private fun startChat() {

        viewLifecycleOwner.lifecycleScope.launch {
            val usrInputText =
                "给我一点学习建议，我的累计打卡次数是${sharedPreferences.getStringList("signed_dates").size},我的学习课程数量是${historyRecordDao.getAllRecords().size},我的学习时间是${studyTime}"
            val myContext = "只能回答教育相关的问题"
            val ret: Int = llm.arun(usrInputText, myContext)
            if (ret != 0) {
                Log.e(TAG, "SparkChain failed:\n$ret")
            }
            sessionFinished = false
        }

    }


    // 显示应用使用时间柱状图
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun displayBarChart() {
        // 选择最近的7天作为展示的数据
        val originalMap: MutableMap<String, Long> = StudyTimeUtils.getDaysUsageTime() // 你的原始map
        val daysUsageTime: Map<String, Long> = if (originalMap.size > 7) {
            originalMap.toList().takeLast(7).toMap()
        } else {
            originalMap.toMap()
        }
        val entries = ArrayList<BarEntry>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sortedDates = daysUsageTime.keys.sorted().map { dateFormat.parse(it) }

        sortedDates.forEachIndexed { index, date ->
            val usageMillis = daysUsageTime[dateFormat.format(date)] ?: 0L
            val usageHours =
                BigDecimal(usageMillis).divide(BigDecimal(3600000), 2, RoundingMode.HALF_UP)
                    .toFloat()
            entries.add(BarEntry(index.toFloat(), usageHours))
        }

        val barChart: BarChart = mBinding.barChart
        val dataSet = BarDataSet(entries, "每日使用时间/小时").apply { }
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
                val index = value.toInt()
                if (index >= 0 && index < sortedDates.size) {
                    val date = sortedDates[index]
                    return monthFormat.format(date)
                }
                return ""
            }
        }
        xAxis.granularity = 1f
        xAxis.labelCount = sortedDates.size
        xAxis.setDrawGridLines(false)

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
        SparkChain.getInst().unInit()
        _mBinding = null
    }


}