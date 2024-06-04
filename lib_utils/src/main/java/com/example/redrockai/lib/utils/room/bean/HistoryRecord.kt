package com.example.redrockai.lib.utils.room.bean
import androidx.room.Entity

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/5/20 20:13
 *  version : 1.0
 *  description :room 的数据类型
 *  saying : 这世界天才那么多，也不缺我一个
 */


@Entity(tableName = "history_records", primaryKeys = ["newsId"])
data class HistoryRecord(
    val newsId: Int, //主键!!
    val title: String,
    val timestamp: Long,
    val playerUrl: String,
    val description: String,
    val coverDetail: String,
    val category: String,
    val shareCount: Int,
    val likeCount: Int,
    val commentCount: Int,
    )