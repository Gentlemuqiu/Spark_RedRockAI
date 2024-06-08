package com.example.redrock.module.video.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.redrock.module.video.R
import com.example.redrock.module.video.ViewModel.IdViewModel
import com.example.redrock.module.video.databinding.ActivityPlayBinding
import com.example.redrock.module.video.ui.fragment.HomeWorkFragment
import com.example.redrock.module.video.ui.fragment.RecommendFragment
import com.example.redrock.module.video.ui.fragment.NoteFragment
import com.example.redrockai.lib.utils.BaseActivity
import com.example.redrockai.lib.utils.adapter.FragmentVpAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import xyz.doikki.videocontroller.StandardVideoController
import xyz.doikki.videocontroller.component.CompleteView
import xyz.doikki.videocontroller.component.ErrorView
import xyz.doikki.videocontroller.component.GestureView
import xyz.doikki.videocontroller.component.VodControlView
import xyz.doikki.videoplayer.player.VideoView

@Route(path = "/play/PlayActivity/")
class PlayActivity : BaseActivity() {

    private lateinit var title: String
    private val mBinding: ActivityPlayBinding by lazy {
        ActivityPlayBinding.inflate(layoutInflater)
    }
    private val idViewModel by lazy { ViewModelProvider(this)[IdViewModel::class.java] }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initVp2()
        initTabLayout()
        initWindow()
        initDate()
    }

    private fun initVp2() {
        mBinding.playVp2.adapter = FragmentVpAdapter(this)
            .add { RecommendFragment() }       // whichPageIsIn = 0
            .add { NoteFragment() }          // whichPageIsIn = 1
            .add { HomeWorkFragment() }    // whichPageIsIn = 2
        // 因为第一页有左滑删除，所以禁止 vp 滑动
        mBinding.playVp2.isUserInputEnabled = false

    }

    private fun initDate() {
        val playUrl = intent.getStringExtra("playUrl")
        val description = intent.getStringExtra("description")
        val category = intent.getStringExtra("category")
        title = intent.getStringExtra("title").toString()
        val shareCount = intent.getIntExtra("shareCount", 0)
        val likeCount = intent.getIntExtra("likeCount", 0)
        val commentCount = intent.getIntExtra("commentCount", 0)
        val id = intent.getIntExtra("id", 0)
        idViewModel.id.value = id
        mBinding.apply {
            tvPlayTitle.text = title
            tvPlayDescription.text = description
            tvPlayCategory.text = category
            tvPlayShare.text = shareCount.toString()
            tvPlayLike.text = likeCount.toString()
            tvPlayComment.text = commentCount.toString()
        }

        initPlayer(playUrl!!)
    }


    private fun initTabLayout() {
        val tabs = arrayOf(
            "推荐",
            "笔记",
            "作业"
        )
        TabLayoutMediator(
            mBinding.playTablayout,
            mBinding.playVp2
        ) { tab, position -> tab.text = tabs[position] }.attach()

        val tab1 = mBinding.playTablayout.getTabAt(0)
        val tab2 = mBinding.playTablayout.getTabAt(1)
        val tab3 = mBinding.playTablayout.getTabAt(2)

        //设置三个tab的自定义View
        val tab1View = LayoutInflater.from(this).inflate(R.layout.play_note_select_tab, null)
        val tab1NOView = LayoutInflater.from(this).inflate(R.layout.play_note_no_select_tab, null)

        tab1?.customView = tab1View
        val tab2NOView = LayoutInflater.from(this).inflate(R.layout.play_helper_select_no_tab, null)
        val tab2View = LayoutInflater.from(this).inflate(R.layout.play_helper_select_tab, null)

        tab2?.customView = tab2NOView
        val tab3NOView =
            LayoutInflater.from(this).inflate(R.layout.play_homework_select_no_tab, null)
        val tab3View = LayoutInflater.from(this).inflate(R.layout.play_homework_select_tab, null)

        tab3?.customView = tab3NOView

        //改变文字颜色
        val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // 切换到选中的Tab
                when (tab.position) {
                    0 -> {
                        tab1?.customView = tab1View
                        tab2?.customView = tab2NOView
                        tab3?.customView = tab3NOView
                    }

                    1 -> {
                        tab1?.customView = tab1NOView
                        tab2?.customView = tab2View
                        tab3?.customView = tab3NOView
                    }

                    2 -> {
                        tab1?.customView = tab1NOView
                        tab2?.customView = tab2NOView
                        tab3?.customView = tab3View
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        tab1?.customView = tab1View
                        tab2?.customView = tab2NOView
                        tab3?.customView = tab3NOView
                    }

                    1 -> {
                        tab1?.customView = tab1NOView
                        tab2?.customView = tab2View
                        tab3?.customView = tab3NOView
                    }

                    2 -> {
                        tab1?.customView = tab1NOView
                        tab2?.customView = tab2NOView
                        tab3?.customView = tab3View
                    }
                }
            }
        }
        mBinding.playTablayout.addOnTabSelectedListener(onTabSelectedListener)
    }

    private fun initWindow() {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.black)
    }

    private fun initPlayer(url: String) {
        val controller = StandardVideoController(this)
        controller.addDefaultControlComponent(title, false)
        //根据屏幕方向自动进入/退出全屏
        controller.setEnableOrientation(false)
        controller.addControlComponent(CompleteView(this)) //自动完成播放界面
        controller.addControlComponent(ErrorView(this)) //错误界面
        controller.addControlComponent(GestureView(this))//滑动控制视图
        controller.addControlComponent(VodControlView(this)) //点播控制视图
        controller.addControlComponent()
        mBinding.player.setVideoController(controller) //设置控制器
        mBinding.player.setScreenScaleType(VideoView.SCREEN_SCALE_16_9)
        mBinding.player.setLooping(true)
        mBinding.player.setUrl(url) //设置视频地址
        mBinding.player.release()
        mBinding.player.start() //开始播放，不调用则不自动播放
    }

    override fun onPause() {
        super.onPause()
        mBinding.player.pause()
    }

    override fun onResume() {
        super.onResume()
        mBinding.player.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.player.release()
    }


    override fun onBackPressed() {
        if (!mBinding.player.onBackPressed()) {
            super.onBackPressed()
        }
    }

}