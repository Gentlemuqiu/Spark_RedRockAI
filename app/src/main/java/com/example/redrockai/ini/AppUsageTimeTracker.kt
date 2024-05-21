package com.example.redrockai.ini

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.Calendar

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/5/21 21:51
 *  version : 1.0
 *  description :
 *  saying : 这世界天才那么多，也不缺我一个
 */
class AppUsageTimeTracker(private val application: Application) {

    private var todayStartTime: Long? = null
    private var todayUsageTime: Long = 0L

    init {
        registerAppLifecycleCallbacks()
        resetTodayUsageTime()
    }

    private fun registerAppLifecycleCallbacks() {
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                // 记录今日第一次启动应用的时间
                if (todayStartTime == null) {
                    resetTodayUsageTime()
                }
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
                // 累计今日使用时间
//                todayUsageTime += System.currentTimeMillis() - todayStartTime!!
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {

            }

            // 其他生命周期方法实现...
        })
    }

    private fun resetTodayUsageTime() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        todayStartTime = calendar.timeInMillis
        todayUsageTime = 0L
    }

    fun getTodayUsageTime(): Long {
        return todayUsageTime
    }
}