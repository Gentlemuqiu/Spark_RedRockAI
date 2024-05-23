package com.example.redrockai.lib.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter


/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-05-21 20:30.
 * @Description : 生活很难，但总有人在爱你。
 */
@RequiresApi(Build.VERSION_CODES.O)
fun formatDateStringWithLocalDate(dateString: String): String {
    // 定义输入和输出格式
    val inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // 解析输入日期字符串
    val date = LocalDate.parse(dateString, inputFormatter)
    // 格式化日期为输出格式
    return date.format(outputFormatter)
}
fun formatNumberToTime(number: Int): String {
    val minutes = number / 60
    val seconds = number % 60
    return "%02d:%02d".format(minutes, seconds)
}
