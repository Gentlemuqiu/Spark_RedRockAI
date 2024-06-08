package com.example.redrockai.lib.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/5/21 21:53
 *  version : 1.0
 *  description :管理学习时间埋点、每天学习时间统计、各天时间统计
 *  saying : 这世界天才那么多，也不缺我一个
 */





object StudyTimeUtils {

    //学习时间
    private val _studiedTime = MutableStateFlow<Long>(0)
    val studiedTime: StateFlow<Long> = _studiedTime.asStateFlow()

    //设置时间
    private val _wishedTime = MutableStateFlow<Long>(0)
    val wishedTime: StateFlow<Long> = _studiedTime.asStateFlow()


    private const val studyTime = "STUDY_TIME"
    private const val lastDay = "LAST_DAY"
    private const val lastStudiedTime = "LAST_STUDIED_TIME"
    private const val want = "WANT"
    private const val allTimeStudiedForMine = "MINE_ALL_TIME"

    private val sharedPreferences =
        BaseApp.getAppContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)


    private var daysUsageTime = mutableMapOf<String, Long>()


    @Synchronized
    fun saveDaysUsageTime(usageTime: Long) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        // 获取已存储的时间数据
        daysUsageTime = getDaysUsageTime()
        //添加使用时间
        daysUsageTime[today] = usageTime
        val jsonString = Gson().toJson(daysUsageTime)
        sharedPreferences.edit().putString("daysUsageTime", jsonString).apply()
    }

    @Synchronized
    fun getDaysUsageTime(): MutableMap<String, Long> {
        val jsonString = sharedPreferences.getString("daysUsageTime", null)
        // 将JSON字符串转换回Map
        return if (jsonString != null) {
            Gson().fromJson(jsonString, object : TypeToken<MutableMap<String, Long>>() {}.type)
        } else {
            mutableMapOf()
        }
    }


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


    @Synchronized
    fun saveMineStudyAllTime(value: Long) {
        sharedPreferences.edit()
            .putString(allTimeStudiedForMine, (getMineStudyAllTime() + value).toString()).apply()
    }

    @Synchronized
    fun getMineStudyAllTime() = sharedPreferences.getString(allTimeStudiedForMine, "0")?.toLong()!!


    //把时间戳变成分钟单位
    fun convertTimestampToMinutes(timestamp: Long) = timestamp / (1000 * 60)


}