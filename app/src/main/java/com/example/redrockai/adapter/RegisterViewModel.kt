package com.example.redrockai.adapter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.redrockai.bean.RegisterRequest
import com.example.redrockai.bean.RegisterResponse
import com.example.redrockai.util.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/8/2 20:08
 *  version : 1.0
 *  description :
 *  saying : 这世界天才那么多，也不缺我一个
 */
class RegisterViewModel : ViewModel() {

    private val _registerResult = MutableLiveData<RegisterResponse>()
    val registerResult: LiveData<RegisterResponse> get() = _registerResult

    fun register(username: String, password: String, avatarUrl: String) {
        val request = RegisterRequest(username, password, avatarUrl)

        RetrofitClient.instance.register(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    _registerResult.postValue(response.body())
                    Log.d("weafwefawfawef", "测试数据${response.body()}")

                } else {
                    _registerResult.postValue(RegisterResponse(-1, "Failed", null))
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _registerResult.postValue(RegisterResponse(-1, "Network Error: ${t.message}", null))
            }
        })
    }


}