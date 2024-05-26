package com.example.redrockai.module.schoolroom.helper.room.db

import android.content.ContentValues
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.redrockai.lib.utils.BaseApp
import com.example.redrockai.module.schoolroom.helper.room.bean.HistoryRecord
import com.example.redrockai.module.schoolroom.helper.room.dao.HistoryRecordDao

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/5/20 20:21
 *  version : 1.0
 *  description :表中存放HistoryRecord数据，如果要迁移数据库请把version++
 *  saying : 这世界天才那么多，也不缺我一个
 */

@Database(entities = [HistoryRecord::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun historyRecordDao(): HistoryRecordDao

    companion object {
        //数据库的名称
        private const val dbName = "past_history_room"

        private var instance: AppDatabase? = null

        //双重检查获取单例顺便初始化
        fun getDatabaseSingleton(): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class.java) {
                    instance = Room.databaseBuilder(
                        BaseApp.getAppContext(),
                        AppDatabase::class.java,
                        dbName
                    )
                        .allowMainThreadQueries()//允许主线程操作
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                Log.d(ContentValues.TAG, "onCreate: db_past")
                            }
                        }) //数据库创建和打开后的回调，可以重写其中的方法
                        .build().also {
                            instance = it
                        }
                }
            }
            return instance!!
        }


    }


}