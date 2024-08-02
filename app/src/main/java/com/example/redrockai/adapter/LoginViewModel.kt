package com.example.redrockai.adapter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.redrockai.bean.LoginRequest
import com.example.redrockai.bean.LoginResponse
import com.example.redrockai.util.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/8/2 20:29
 *  version : 1.0
 *  description :
 *  saying : 这世界天才那么多，也不缺我一个
 */
class LoginViewModel : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> get() = _loginResult


    fun register(username: String, password: String) {
        val request = LoginRequest(username, password)

        RetrofitClient.instance.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    _loginResult.postValue(response.body())
                    Log.d("weafwefawfawef", "测试数据${response.body()}")
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("LogTwefawfewest", "测试数据${t.message.toString()}")
            }
        })
    }

}