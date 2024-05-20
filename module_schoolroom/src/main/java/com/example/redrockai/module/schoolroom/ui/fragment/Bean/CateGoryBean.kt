package com.example.redrockai.module.schoolroom.ui.fragment.Bean

import com.google.gson.annotations.SerializedName

class CateGoryBean : ArrayList<CateGoryBean.CateGoryBeanItem>() {
    data class CateGoryBeanItem(
        @SerializedName("alias")
        val alias: Any, // null
        @SerializedName("bgColor")
        val bgColor: String,
        @SerializedName("bgPicture")
        val bgPicture: String, // http://img.kaiyanapp.com/57472e13fd2b6c9655c8a600597daf4d.png?imageMogr2/quality/60/format/jpg
        @SerializedName("defaultAuthorId")
        val defaultAuthorId: Int, // 2162
        @SerializedName("description")
        val description: String, // 为广告人的精彩创意点赞
        @SerializedName("headerImage")
        val headerImage: String, // http://img.kaiyanapp.com/fc228d16638214b9803f46aabb4f75e0.png
        @SerializedName("id")
        val id: Int, // 14
        @SerializedName("name")
        val name: String, // 广告
        @SerializedName("tagId")
        val tagId: Int // 16
    )
}