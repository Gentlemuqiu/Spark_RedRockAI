package com.example.redrockai.module.schoolroom.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.redrockai.lib.utils.BaseActivity
import com.example.redrockai.module.schoolroom.R
import com.example.redrockai.module.schoolroom.helper.room.bean.HistoryRecord
import com.example.redrockai.module.schoolroom.helper.room.dao.HistoryRecordDao
import com.example.redrockai.module.schoolroom.helper.room.db.AppDatabase
import kotlinx.coroutines.launch

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/5/20 20:03
 *  version : 1.0
 *  description :历史记录activity
 *  saying : 这世界天才那么多，也不缺我一个
 */
class HistoryRecordActivity : BaseActivity() {

    //历史消息room的dao接口
    private lateinit var historyRecordDao: HistoryRecordDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        iniRoom()




    }

    private fun iniRoom() {
        historyRecordDao = AppDatabase.getDatabaseSingleton().historyRecordDao()
    }
}