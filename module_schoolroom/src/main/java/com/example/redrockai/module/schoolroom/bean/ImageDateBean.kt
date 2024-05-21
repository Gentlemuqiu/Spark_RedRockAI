package com.example.redrockai.module.schoolroom.bean

import com.google.gson.annotations.SerializedName


/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-05-21 18:48.
 * @Description : 生活很难，但总有人在爱你。
 */
data class ImageDateBean(
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
        @SerializedName("copyright")
        val copyright: String,
        @SerializedName("copyrightlink")
        val copyrightlink: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("time")
        val time: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("urlbase")
        val urlbase: String,
        @SerializedName("urls")
        val urls: List<String>
    )
}