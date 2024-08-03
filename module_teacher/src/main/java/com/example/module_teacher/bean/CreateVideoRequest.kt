package com.example.module_teacher.bean

/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-08-03 14:27.
 * @Description : 生活很难，但总有人在爱你。
 */

data class CreateVideoRequest(
    val tag_id: Int,
    val title: String,
    val desc: String,
    val play_url: String,
    val cover_url: String
)
