package com.example.redrockai.apiservice

import com.example.redrockai.bean.LoginRequest
import com.example.redrockai.bean.LoginResponse
import com.example.redrockai.bean.RegisterRequest
import com.example.redrockai.bean.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/8/2 20:19
 *  version : 1.0
 *  description :
 *  saying : 这世界天才那么多，也不缺我一个
 */


interface ApiService {


    @POST("/register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>


}
