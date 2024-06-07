package com.example.redrockai.lib.utils

import android.content.SharedPreferences

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/6/7 15:06
 *  version : 1.0
 *  description :
 *  saying : 这世界天才那么多，也不缺我一个
 */

//一些扩展函数
fun SharedPreferences.putStringList(key: String, list: List<String>) {
    val editor = this.edit()
    editor.putString(key, list.joinToString(","))
    editor.apply()
}

fun SharedPreferences.getStringList(key: String): List<String> {
    val value = this.getString(key, "") ?: ""
    return if (value.isEmpty()) {
        emptyList()
    } else {
        value.split(",")
    }
}

object SighInUtils {
    //一次app生命周期只弹出来一次
    var haveToast: Boolean? = false


}