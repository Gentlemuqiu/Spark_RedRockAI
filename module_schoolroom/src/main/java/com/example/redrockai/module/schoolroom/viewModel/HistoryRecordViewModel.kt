package com.example.redrockai.module.schoolroom.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redrockai.module.schoolroom.helper.room.bean.HistoryRecord
import com.example.redrockai.module.schoolroom.helper.room.dao.HistoryRecordDao
import com.example.redrockai.module.schoolroom.helper.room.db.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/5/21 10:27
 *  version : 1.0
 *  description :历史学习的vh
 *  saying : 这世界天才那么多，也不缺我一个
 */
class HistoryRecordViewModel : ViewModel() {

    //历史消息room的dao接口
    private lateinit var historyRecordDao: HistoryRecordDao

    //历史数据
    private val _historyRecords = MutableStateFlow<List<HistoryRecord>>(emptyList())
    val historyRecords: StateFlow<List<HistoryRecord>> = _historyRecords.asStateFlow()


    init {
        iniRoom()
        iniPastData()
    }

    private fun iniRoom() {
        historyRecordDao = AppDatabase.getDatabaseSingleton().historyRecordDao()

    }


    private fun iniPastData() {
        viewModelScope.launch {
            _historyRecords.value = historyRecordDao.getAllRecords()
        }
    }

    //拿到搜索数据
    fun getSearchData(keyword:String) {
        viewModelScope.launch {
            _historyRecords.value = historyRecordDao.getRecordsByTitleKeyword(keyword)
        }
    }


}