package com.example.redrockai.bean

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/8/2 20:17
 *  version : 1.0
 *  description :
 *  saying : 这世界天才那么多，也不缺我一个
 */
data class RegisterRequest(
    val username: String,
    val password: String,
    val avatar_url: String
)