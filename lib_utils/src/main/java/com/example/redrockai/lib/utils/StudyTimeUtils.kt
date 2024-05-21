package com.example.redrockai.lib.utils

import android.content.Context

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/5/21 21:53
 *  version : 1.0
 *  description :管理学习时间埋点
 *  saying : 这世界天才那么多，也不缺我一个
 */
object StudyTimeUtils {

    private const val studyTime = "STUDY_TIME"

    private val sharedPreferences =
        BaseApp.getAppContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)


    @Synchronized
    fun saveStudyTime(value: String) {
        sharedPreferences.edit().putString(studyTime, value).apply()
    }

    @Synchronized
    fun getStudyTime(): String {
        return sharedPreferences.getString(studyTime, "0")!!
    }


}