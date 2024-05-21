package com.example.redrockai.module.schoolroom.helper.date

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/5/21 17:25
 *  version : 1.0
 *  description :
 *  saying : 这世界天才那么多，也不缺我一个
 */
object DateTransformer {

    fun formatTimestamp(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date(timestamp)
        return dateFormat.format(date)
    }

}