package com.example.redrockai

import com.alibaba.android.arouter.launcher.ARouter
import com.example.redrockai.ini.AppSignInTracker
import com.example.redrockai.ini.AppUsageTimeTracker
import com.example.redrockai.lib.utils.BaseApp
import com.example.redrockai.lib.utils.BaseUtils.shortToast
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechUtility
import com.iflytek.sparkchain.core.SparkChain
import com.iflytek.sparkchain.core.SparkChainConfig

class App : BaseApp() {
    companion object {
        lateinit var mContext: App
    }

    private var isDebugARouter = true//ARouter调试开关
    override fun onCreate() {
        SpeechUtility.createUtility(this, SpeechConstant.APPID+"=da2f95cc")
        super.onCreate()
        initArouter()
        initSDK()
        initStudyTime()
        initAppSighInTracker()
    }


    private fun initArouter() {
        mContext = this
        //初始化ARouter框架
        if (isDebugARouter) {
            //下面两行必须写在init之前，否则这些配置在init中将无效
            ARouter.openLog()
            //开启调试模式（如果在InstantRun模式下运行，必须开启调试模式！
            // 线上版本需要关闭，否则有安全风险）
            ARouter.openDebug()
        }
        //官方推荐放到Application中初始化
        ARouter.init(mContext)
    }

    private fun initSDK() {
        // 初始化SDK，App id等信息在清单中配置
        val sparkChainConfig: SparkChainConfig = SparkChainConfig.builder()
        sparkChainConfig.appID("da2f95cc").apiKey("fac412ab739047c09f08d5360592cb1c")
            .apiSecret("YTNiNzQ1ZDk5ZTY1ZGJiZTQ5ODRmN2Ni").logLevel(0)
        val ret: Int = SparkChain.getInst().init(applicationContext, sparkChainConfig)
        if (ret == 0) {
            shortToast("SDK初始化成功：$ret")
        } else {
            shortToast("SDK初始化失败-其他错误：$ret")
        }
    }


    //初始化时间学习埋点
    private fun initStudyTime() {
        AppUsageTimeTracker(this)
    }

    //全局检测是否打卡成功
    private fun initAppSighInTracker() {
        AppSignInTracker()
    }


}