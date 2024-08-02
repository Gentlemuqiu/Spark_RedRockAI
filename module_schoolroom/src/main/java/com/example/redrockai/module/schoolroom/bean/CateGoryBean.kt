package com.example.redrockai.module.schoolroom.bean

import com.google.gson.annotations.SerializedName


data class CateGoryBean(
    @SerializedName("tag")
    val tag: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("msg")
    val error: Any
) {
    data class Data(
        @SerializedName("tag_id")
        val tagId: Int,
        @SerializedName("tag_name")
        val tagName: String

    )

}