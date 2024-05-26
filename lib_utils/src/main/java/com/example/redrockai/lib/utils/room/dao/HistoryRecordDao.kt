package com.example.redrockai.module.schoolroom.helper.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.redrockai.module.schoolroom.helper.room.bean.HistoryRecord

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/5/20 20:14
 *  version : 1.0
 *  description :
 *  saying : 这世界天才那么多，也不缺我一个
 */
@Dao
interface HistoryRecordDao {


    // 查询是否存在当前的新闻记录，不存在则返回为空
    // todo：你应该先进行插入前的检查或者使用insertOrUpdate 这个api
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: HistoryRecord)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(record: HistoryRecord)

    //上面两个升级版，也可用这个api不用判断
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(record: HistoryRecord)

    @Delete
    suspend fun delete(record: HistoryRecord)

    //删除指定id的数据
    @Query("DELETE FROM history_records WHERE newsId = :newsId")
    suspend fun deleteRecordByNewsId(newsId: Int)


    @Query("SELECT * FROM history_records WHERE newsId = :newsId")
    suspend fun getRecordByNewsId(newsId: Int): HistoryRecord?


    //按照时间戳的降序排列（也就是由新到旧）
    @Query("SELECT * FROM history_records ORDER BY timestamp DESC")
    suspend fun getAllRecords(): List<HistoryRecord>

    // 按照id查询记录，返回值可能为空
    @Query("SELECT * FROM history_records WHERE newsId = :newsId")
    suspend fun getRecordById(newsId: Long): HistoryRecord?

    //模糊搜索，根据标题的关键字
    @Query("SELECT * FROM history_records WHERE title LIKE '%' || :keyword || '%' ORDER BY timestamp DESC")
    suspend fun getRecordsByTitleKeyword(keyword: String): List<HistoryRecord>

}