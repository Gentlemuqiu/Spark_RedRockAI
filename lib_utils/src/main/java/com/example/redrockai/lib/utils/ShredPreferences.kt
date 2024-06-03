package com.example.redrockai.lib.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-06-03 13:15.
 * @Description : 生活很难，但总有人在爱你。
 */

fun getSp(name: String): SharedPreferences {
    return BaseApp.getAppContext().getSharedPreferences(name, Context.MODE_PRIVATE)
}

val gson: Gson by lazy { Gson() }

/**
 * 将对象转换成为字符串然后存进sp中
 */
fun <T> gsonSaveToSp(value: T, sharedPreferencesName: String, key: String? = null) {
    val str = gson.toJson(value)
    val edit = getSp(sharedPreferencesName).edit()
    edit.putString(key ?: sharedPreferencesName, str)
    edit.apply()
}

/**
 * 从sp中取出字符串，并且解析成为对象。由于泛型的类型擦除，所以用内联类和reified(内联类中获取泛型信息的)来解决
 */
inline fun <reified T> objectFromSp(sharedPreferencesName: String, key: String? = null): T? {
    val sp = getSp(sharedPreferencesName)
    val str = sp.getString(key ?: sharedPreferencesName, null) ?: return null
    return gson.fromJson(str, T::class.java)
}