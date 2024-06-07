package com.example.redrockai.ini

import android.content.Context
import android.util.Log
import com.example.redrockai.lib.utils.BaseApp
import com.example.redrockai.lib.utils.BaseUtils
import com.example.redrockai.lib.utils.SighInUtils
import com.example.redrockai.lib.utils.StudyTimeUtils
import com.example.redrockai.lib.utils.getStringList
import com.example.redrockai.lib.utils.putStringList
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.launch

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/6/7 20:14
 *  version : 1.0
 *  description :
 *  saying : 这世界天才那么多，也不缺我一个
 */
class AppSignInTracker {

    private val signedDates = mutableSetOf<CalendarDay>()

    private val sharedPreferences =
        BaseApp.getAppContext().getSharedPreferences("SignInPrefs", Context.MODE_PRIVATE)

    init {
        initListener()
    }


    private fun initListener() {

        //先同步已经打卡的日期
        readSignedDates()

        //当学习的时间大于设置的时间的时候自动打卡
        BaseApp.applicationScope.launch {
            BaseApp
            StudyTimeUtils.studiedTime.collect {
                val today = CalendarDay.today()
                Log.d("wefawef", "测试数据${today}")
                if (StudyTimeUtils.convertTimestampToMinutes(it) >= StudyTimeUtils.getWantedStudiedTime()
                        .toLong() && SighInUtils.haveToast == false
                ) {
                    //签到成功
                    BaseUtils.shortToast("恭喜您打卡成功")
                    SighInUtils.haveToast = true
                    if (signedDates.add(today)) {
                        // 保存已签到日期
                        saveSignedDates()
                    }
                }
            }
        }
    }


    //读取已经签到的日子,保持同步
    private fun readSignedDates() {

        val dateList = sharedPreferences.getStringList("signed_dates")
        dateList.forEach { dateString ->
            val parts = dateString.split("-")
            if (parts.size == 3) {
                val year = parts[0].toInt()
                val month = parts[1].toInt()
                val day = parts[2].toInt()
                signedDates.add(CalendarDay.from(year, month, day))
            }
        }

    }

    //保存已经签到的日子
    private fun saveSignedDates() {
        val dateList = signedDates.map { "${it.year}-${it.month}-${it.day}" }
        sharedPreferences.putStringList("signed_dates", dateList)
    }
}