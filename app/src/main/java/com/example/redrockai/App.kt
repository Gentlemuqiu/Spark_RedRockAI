package com.example.redrockai

import com.example.redrockai.lib.utils.BaseApp
import com.example.redrockai.lib.utils.BaseUtils.shortToast
import com.iflytek.sparkchain.core.SparkChain
import com.iflytek.sparkchain.core.SparkChainConfig

class App : BaseApp() {

    override fun onCreate() {
        super.onCreate()
        initSDK()
    }

    private fun initSDK() {
        // 初始化SDK，App id等信息在清单中配置
        val sparkChainConfig: SparkChainConfig = SparkChainConfig.builder()
        sparkChainConfig.appID("da2f95cc")
            .apiKey("fac412ab739047c09f08d5360592cb1c")
            .apiSecret("YTNiNzQ1ZDk5ZTY1ZGJiZTQ5ODRmN2Ni")
            .logLevel(0)
        val ret: Int = SparkChain.getInst().init(applicationContext, sparkChainConfig)
        if (ret == 0) {
            shortToast("SDK初始化成功：$ret")
        } else {
            shortToast("SDK初始化失败-其他错误：$ret")
        }
    }

}