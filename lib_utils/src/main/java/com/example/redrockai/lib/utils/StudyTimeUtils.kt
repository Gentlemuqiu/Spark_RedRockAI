package com.example.redrockai.lib.utils

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/5/21 21:53
 *  version : 1.0
 *  description :管理学习时间埋点
 *  saying : 这世界天才那么多，也不缺我一个
 */
object StudyTimeUtils {

    //学习时间
    private val _studiedTime = MutableStateFlow<Long>(0)
    val studiedTime: StateFlow<Long> = _studiedTime.asStateFlow()

    private const val studyTime = "STUDY_TIME"
    private const val lastDay = "LAST_DAY"
    private const val lastStudiedTime = "LAST_STUDIED_TIME"
    private const val want = "WANT"

    private val sharedPreferences =
        BaseApp.getAppContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)


    @Synchronized
    fun saveLastStudiedTime(value: String) {
        sharedPreferences.edit().putString(lastStudiedTime, value).apply()
    }

    @Synchronized
    fun getLastStudiedTime() = sharedPreferences.getString(lastStudiedTime, "0")!!

    @Synchronized
    fun saveLastDay(value: String) {
        sharedPreferences.edit().putString(lastDay, value).apply()
    }


    /**
     * 上一天的信息
     */
    @Synchronized
    fun getLastDay(): String {
        return sharedPreferences.getString(lastDay, "1621769124000")!!
    }

    fun saveStudiedTime(value: Long) {
        _studiedTime.value = value
    }

    @Synchronized
    fun saveWantedStudiedTime(value: String) {
        sharedPreferences.edit().putString(want, value).apply()
    }

    @Synchronized
    fun getWantedStudiedTime(): String = sharedPreferences.getString(want, "60")!!


    //把时间戳变成分钟单位
    fun convertTimestampToMinutes(timestamp: Long) = timestamp / (1000 * 60)


}