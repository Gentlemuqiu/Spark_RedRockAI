package com.example.redrockai.util

import android.content.Context
import android.util.Base64
import android.util.Log
import com.example.redrockai.lib.utils.BaseApp
import com.google.gson.Gson
import org.json.JSONObject

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/4/22 17:37
 *  version : 1.0
 *  description :
 *  saying : 这世界天才那么多，也不缺我一个
 */
object SPUtils {

    private const val mail = "MAIL"
    private const val password = "PASSWORD"
    private const val token = "TOKEN"
    private const val freshToken = "FRESH"
    private const val hb = "HBDEVICE"
    private const val fall = "FALLDEVICE"
    private const val login = "LOGIN"
    private const val nickName = "NICKNAME"

    private val gson = Gson()

    private val sharedPreferences =
        BaseApp.getAppContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)


    @Synchronized
    fun saveUserName(value: String) {
        sharedPreferences.edit().putString(mail, value).apply()
    }

    @Synchronized
    fun savePassWord(value: String) {
        sharedPreferences.edit().putString(password, value).apply()
    }

    @Synchronized
    fun saveToken(value: String) {
        sharedPreferences.edit().putString(token, value).apply()
    }

    @Synchronized
    fun saveRefreshToken(value: String) {
        sharedPreferences.edit().putString(freshToken, value).apply()
    }

    @Synchronized
    fun saveHbMessage(value: String) {
        sharedPreferences.edit().putString(hb, value).apply()
    }

    @Synchronized
    fun saveFallMessage(value: String) {
        sharedPreferences.edit().putString(fall, value).apply()
    }

    //调用则为登录。
    @Synchronized
    fun saveLoginStatus() {
        sharedPreferences.edit().putString(login, "1").apply()
    }

    @Synchronized
    fun saveNickName(token: String) {
        var result = "用户"
        try {
            val splitToken = token.split(".")
            if (splitToken.size != 3) {
                sharedPreferences.edit().putString(nickName, result).apply()
                return
            }
            val base64EncodedPayload = splitToken[1]
            val decodedBytes = Base64.decode(base64EncodedPayload, Base64.DEFAULT)
            val payload = String(decodedBytes)
            val jsonObject = JSONObject(payload)
            result =
                jsonObject.getString("nickname")
            Log.d("ewfaef", "测试数据${result}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        sharedPreferences.edit().putString(nickName, result).apply()
    }

    @Synchronized
    fun getMail() = sharedPreferences.getString(mail, "")!!

    @Synchronized
    fun getPassWord() = sharedPreferences.getString(password, "")!!

    @Synchronized
    fun getToken() = sharedPreferences.getString(token, "")!!

    @Synchronized
    fun getRefreshToken() = sharedPreferences.getString(freshToken, "")!!



    //true 已经登录
    @Synchronized
    fun getLoginStatus(): Boolean {
        val result = sharedPreferences.getString(login, "0")
        return result == "1"
    }

    @Synchronized
    fun getNickName(): String {
        return sharedPreferences.getString(nickName, "用户")!!
    }

    @Synchronized
    fun clearAllData() {
        sharedPreferences.edit().clear().apply()
    }


}