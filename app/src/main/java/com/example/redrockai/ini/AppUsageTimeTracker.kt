package com.example.redrockai.ini

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.example.redrockai.lib.utils.StudyTimeUtils
import com.example.redrockai.lib.utils.StudyTimeUtils.saveLastStudiedTime
import com.example.redrockai.lib.utils.StudyTimeUtils.saveMineStudyAllTime
import com.example.redrockai.lib.utils.StudyTimeUtils.saveStudiedTime
import java.util.Date

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/5/21 21:51
 *  version : 1.0
 *  description :
 *  saying : 这世界天才那么多，也不缺我一个
 */
class AppUsageTimeTracker(private val application: Application) {


    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {

            totalTime = StudyTimeUtils.getLastStudiedTime()
                .toLong() + (System.currentTimeMillis() - startTime)
            startTime = System.currentTimeMillis()
            //flow更新ui
            saveStudiedTime(totalTime)
            //更新上次学习时间
            saveLastStudiedTime(totalTime.toString())

            //记录每天的学习时间，用于各天展示
            StudyTimeUtils.saveDaysUsageTime(totalTime)

            //开启下一次的循环
            sendEmptyMessageDelayed(0, 1000)
        }

    }

    //计时的起点
    private var startTime: Long = 0

    private var totalTime: Long = 0

    init {
        checkNeedReset()
        registerAppLifecycleCallbacks()
        handler.sendEmptyMessageDelayed(0, 1000)
    }

    /**
     * 正常逻辑，我们要判断是否需要重置时间
     * 因为每隔一天学习时间要重置
     */
    private fun checkNeedReset() {
        // 两个时间戳(毫秒)昨天和今天
        val timestamp1 = StudyTimeUtils.getLastDay().toLong()//last
        val timestamp2 = System.currentTimeMillis() // 现在的时间戳

        // 将时间戳转换为 Date 对象
        val date1 = Date(timestamp1)
        val date2 = Date(timestamp2)

        // 获取年、月、日
        val year1 = date1.year + 1900 // Date 类的 year 字段表示从 1900 年开始的年数
        val month1 = date1.month + 1 // Date 类的 month 字段是从 0 开始计算的
        val day1 = date1.date

        val year2 = date2.year + 1900
        val month2 = date2.month + 1
        val day2 = date2.date

        // 判断是否同一天且 timestamp2 > timestamp1
        //如果是同一天
        if (year1 == year2 && month1 == month2 && day1 == day2) {
            //更新起点
            startTime = System.currentTimeMillis()


        } else {
            //如果不是同一天，那么从此刻开始计时
            startTime = System.currentTimeMillis()
            //更新last
            StudyTimeUtils.saveLastDay(System.currentTimeMillis().toString())
            //上次学习时间为0(这个是间隔)
            saveLastStudiedTime("0")
            //mine模块的学习总时间更新
            saveMineStudyAllTime(StudyTimeUtils.getLastStudiedTime().toLong())
            Log.d("ewfwefwe", "测试数据bu同一天")

        }


    }

    private fun registerAppLifecycleCallbacks() {
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
                //更新为上次的时间
                saveStudiedTime(StudyTimeUtils.getLastStudiedTime().toLong())
                saveMineStudyAllTime(StudyTimeUtils.getLastStudiedTime().toLong())

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {
                totalTime = StudyTimeUtils.getLastStudiedTime()
                    .toLong() + (System.currentTimeMillis() - startTime)
                startTime = System.currentTimeMillis()
                //flow更新ui
                saveStudiedTime(totalTime)
                //更新上次学习时间
                saveLastStudiedTime(totalTime.toString())


            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                handler.removeCallbacksAndMessages(null)
            }

        })
    }


}