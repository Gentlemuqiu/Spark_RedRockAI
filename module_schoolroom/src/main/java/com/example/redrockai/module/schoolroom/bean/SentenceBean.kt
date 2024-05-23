package com.example.redrockai.module.schoolroom.bean

import com.google.gson.annotations.SerializedName


/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-05-23 00:17.
 * @Description : 生活很难，但总有人在爱你。
 */
data class SentenceBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: Any,
    @SerializedName("updateTime")
    val updateTime: Long
) {
    data class Data(
        @SerializedName("content")
        val content: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("origin")
        val origin: String,
        @SerializedName("tag")
        val tag: String,
        @SerializedName("updated_at")
        val updatedAt: String
    )
}