package com.example.redrock.module.video.Bean

import com.google.gson.annotations.SerializedName

data class RelatedCategoryBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("msg")
    val msg: String
) {

    data class Data(
        @SerializedName("cover_url")
        val coverUrl: String,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("play_url")
        val playUrl: String,
        @SerializedName("tag_id")
        val tagId: Int,
        @SerializedName("title")
        val title: String
    )
}