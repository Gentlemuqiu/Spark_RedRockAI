package com.example.redrockai.module.schoolroom.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.example.redrockai.lib.utils.BaseUtils.shortToast
import com.example.redrockai.lib.utils.StudyTimeUtils
import com.example.redrockai.lib.utils.StudyTimeUtils.convertTimestampToMinutes
import com.example.redrockai.lib.utils.StudyTimeUtils.getWantedStudiedTime
import com.example.redrockai.lib.utils.StudyTimeUtils.saveWantedStudiedTime
import com.example.redrockai.lib.utils.formatDateStringWithLocalDate
import com.example.redrockai.lib.utils.room.dao.HistoryRecordDao
import com.example.redrockai.lib.utils.room.db.AppDatabase
import com.example.redrockai.lib.utils.view.EditDialog
import com.example.redrockai.module.schoolroom.adapter.RelatedIntroduceAdapter
import com.example.redrockai.module.schoolroom.bean.CateGoryBean
import com.example.redrockai.module.schoolroom.databinding.FragmentClassBinding
import com.example.redrockai.module.schoolroom.ui.activity.HistoryRecordActivity
import com.example.redrockai.module.schoolroom.viewModel.CateGoryViewModel
import com.example.redrockai.module.schoolroom.viewModel.ImageViewModel
import com.example.redrockai.module.schoolroom.viewModel.IntroduceViewModel
import com.example.redrockai.module.schoolroom.viewModel.SentenceViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch


class ClassFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private var _mBinding: FragmentClassBinding? = null
    private val mBinding: FragmentClassBinding
        get() = _mBinding!!
    private val newFollowViewModel by lazy {
        ViewModelProvider(this)[CateGoryViewModel::class.java]
    }
    private val introduceViewModel by lazy {
        ViewModelProvider(this)[IntroduceViewModel::class.java]
    }
    private val imageViewModel by lazy {
        ViewModelProvider(this)[ImageViewModel::class.java]
    }
    private val sentenceViewModel by lazy {
        ViewModelProvider(this)[SentenceViewModel::class.java]
    }
    private lateinit var adapter: RelatedIntroduceAdapter
    private lateinit var list: List<CateGoryBean.Data>
    private lateinit var word: String

    //历史消息room的dao接口
    private lateinit var historyRecordDao: HistoryRecordDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentClassBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniView()
        iniTabLayout()
        initSchoolRoomRv()
        initClickListener()
        initImage()
        initStudiedTime()
        initChangeStudiedTime()


    }

    @SuppressLint("SetTextI18n")
    private fun initChangeStudiedTime() {
        mBinding.apply {
            courseChangeStudyTime.setOnClickListener {
                EditDialog.Builder(
                    requireContext(),
                    EditDialog.DataImpl(
                        content = "",
                        hint = "分钟",
                        width = 255,
                        height = 207
                    )
                ).setPositiveClick {
                    //正整数
                    if (isPositiveInteger(getInput())) {
                        saveWantedStudiedTime(getInput())
                        courseExactlyEditTime.text = getWantedStudiedTime().plus("分钟")
                        shortToast("设置成功")
                        dismiss()
                    } else {
                        shortToast("输入不合理,请输入正整数时间")
                    }

                }.setNegativeClick {
                    dismiss()
                }.show()

            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initImage() {
        imageViewModel.getImage()
        imageViewModel.imageDate.observe(viewLifecycleOwner) {
            Glide.with(this).load(it.data.urls.get(11)).into(mBinding.ivBing)
            mBinding.tvDetail.text = it.data.title


            mBinding.tvData.text = formatDateStringWithLocalDate(it.data.time)
        }
    }


    /**
     * 初始化一些操作
     */
    private fun iniView() {
        newFollowViewModel.getCateGory()
        historyRecordDao = AppDatabase.getDatabaseSingleton().historyRecordDao()
        mBinding.mcClick.setOnClickListener { showDialog() }
        sentenceViewModel.getSentence()
        sentenceViewModel.sentenceData.observe(viewLifecycleOwner) {
            word = it.data.content
        }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("一言")
        builder.setMessage(word)
        builder.setPositiveButton("希望满满!!!") { dialog, which ->
        }
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun initSchoolRoomRv() {
        adapter = RelatedIntroduceAdapter().apply {
            setOnClassItemClickListener {
//                // 生成一条记录
//                val record = HistoryRecord(
//                    newsId = it.data.content.data.id,
//                    title = it.data.content.data.title,
//                    timestamp = System.currentTimeMillis(),
//                    playerUrl = it.data.content.data.playUrl,
//                    description = it.data.content.data.description,
//                    coverDetail = it.data.content.data.cover.detail,
//                    category = it.data.content.data.category,
//                    shareCount = it.data.content.data.consumption.shareCount,
//                    likeCount = it.data.content.data.consumption.realCollectionCount,
//                    commentCount = it.data.content.data.consumption.replyCount
//
//                )
//                GlobalScope.launch {
//                    historyRecordDao.insertOrUpdate(record)
//                }

               ARouter.getInstance().build("/play/PlayActivity/")
                   .withString("playUrl", it.playUrl)
                   .withString("title", it.title)
                   .withString("description", it.desc)
                   .withString("coverDetail", it.coverUrl)
                   .withInt("id", it.playUrl.hashCode())
                    .navigation(context)
            }
        }
        newFollowViewModel.cateGoryData.observe(viewLifecycleOwner) {
            val categories = it
            list = categories.data
            categories.data.forEach { category ->
                // 在这里处理每个元素，
                val tab = tabLayout.newTab()
                tab.text = category.tagName
                tabLayout.addTab(tab)
                Log.d("hui", "initSchoolRoomRv: ${category.tagName}")
            }
        }
        introduceViewModel.relatedCategoryData.observe(viewLifecycleOwner) {
            adapter.submitList(it.data)
        }
        mBinding.recyclerviewIntroduce.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        mBinding.recyclerviewIntroduce.adapter = adapter
    }

    private fun iniTabLayout() {
        mBinding.apply {
            tabLayout = classTablayout
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val selectedTabIndex = tab?.position ?: -1
                    introduceViewModel.getRelatedCategoryData(list[selectedTabIndex].tagId)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // 当Tab取消选中时的处理
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // 当Tab重新选中时的处理
                }
            })
        }
    }

    private fun initClickListener() {
        mBinding.apply {
            coursePast.setOnClickListener {
                startActivity(Intent(requireContext(), HistoryRecordActivity::class.java))
            }
        }

    }

    private fun initStudiedTime() {

        viewLifecycleOwner.lifecycleScope.launch {
            StudyTimeUtils.studiedTime.collect {

                mBinding.apply {
                    courseStudiedTime.text =
                        "今日已学".plus(convertTimestampToMinutes(it).toString()).plus("/")
                }

            }
        }

        mBinding.courseExactlyEditTime.text = getWantedStudiedTime().plus("分钟")


    }

    override fun onResume() {
        mBinding.courseExactlyEditTime.text = getWantedStudiedTime().plus("分钟")
        super.onResume()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }


}