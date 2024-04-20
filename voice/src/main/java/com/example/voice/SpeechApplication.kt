package com.example.voice

/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-04-19 11:24.
 * @Description : 生活很难，但总有人在爱你。
 */
import android.app.Application
import com.iflytek.cloud.SpeechUtility

class SpeechApplication : Application() {
    override fun onCreate() {
        //   5ef048e1  为在开放平台注册的APPID  注意没有空格，直接替换即可
        SpeechUtility.createUtility(this@SpeechApplication, "appid=da2f95cc")
        super.onCreate()
    }
}
