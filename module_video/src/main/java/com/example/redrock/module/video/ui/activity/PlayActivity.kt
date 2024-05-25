package com.example.redrock.module.video.ui.activity

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.model.play.Adapter.TopAdapter
import com.example.model.play.ViewModel.RelatedViewModel
import com.example.redrock.module.video.R
import com.example.redrock.module.video.databinding.ActivityMainBinding
import com.example.redrock.module.video.ui.fragment.HomeWorkFragment
import com.example.redrock.module.video.ui.fragment.NoteFragment
import com.example.redrock.module.video.ui.fragment.StudyHelperFragment
import com.example.redrockai.lib.utils.adapter.FragmentVpAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import xyz.doikki.videocontroller.StandardVideoController
import xyz.doikki.videocontroller.component.CompleteView
import xyz.doikki.videocontroller.component.ErrorView
import xyz.doikki.videocontroller.component.GestureView
import xyz.doikki.videocontroller.component.VodControlView
import xyz.doikki.videoplayer.player.VideoView

class PlayActivity : AppCompatActivity() {

    private lateinit var title: String
    private val mBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initVp2()
        initTabLayout()
        initWindow()
        initDate()
    }

    private fun initVp2() {
        mBinding.playHomeVp2.adapter = FragmentVpAdapter(this)
            .add { NoteFragment() }       // whichPageIsIn = 0
            .add { StudyHelperFragment() }          // whichPageIsIn = 1
            .add { HomeWorkFragment() }    // whichPageIsIn = 2
        // 因为第一页有左滑删除，所以禁止 vp 滑动
        mBinding.playHomeVp2.isUserInputEnabled = false

    }

    private fun initDate() {
        //  val playUrl = intent.getStringExtra("playUrl")
        //  val title = intent.getStringExtra("title")
        //  val description = intent.getStringExtra("description")
        //  val category = intent.getStringExtra("category")

        val playUrl =
            "http://baobab.kaiyanapp.com/api/v1/playUrl?vid=8810&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid="
        title = "你好"
        val description = "很好"
        val category = "我也好"

        val shareCount = intent.getIntExtra("shareCount", 0)
        val likeCount = intent.getIntExtra("likeCount", 0)
        val commentCount = intent.getIntExtra("commentCount", 0)
        var id = intent.getIntExtra("id", 0)

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
            "笔记",
            "学习助手",
            "作业"
        )
        TabLayoutMediator(
            mBinding.classTablayout,
            mBinding.playHomeVp2
        ) { tab, position -> tab.text = tabs[position] }.attach()

        val tab1 = mBinding.classTablayout.getTabAt(0)
        val tab2 = mBinding.classTablayout.getTabAt(1)
        val tab3 = mBinding.classTablayout.getTabAt(2)

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
        mBinding.classTablayout.addOnTabSelectedListener(onTabSelectedListener)
    }

    private fun initWindow() {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.black)
    }

    private fun initPlayer(url: String) {
        val controller = StandardVideoController(this)
        controller.addDefaultControlComponent(title, false)
        controller.setEnableOrientation(false)
        controller.addControlComponent(CompleteView(this)) //自动完成播放界面
        controller.addControlComponent(ErrorView(this)) //错误界面
        controller.addControlComponent(GestureView(this))//滑动控制视图
        controller.addControlComponent(VodControlView(this)) //点播控制视图
        mBinding.player.setVideoController(controller) //设置控制器
        mBinding.player.setScreenScaleType(VideoView.SCREEN_SCALE_16_9)
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