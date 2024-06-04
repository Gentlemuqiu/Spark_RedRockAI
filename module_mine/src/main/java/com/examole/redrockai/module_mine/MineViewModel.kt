package com.examole.redrockai.module_mine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.examole.redrockai.module_mine.bean.MineData
import com.example.redrockai.lib.utils.StudyTimeUtils
import com.example.redrockai.lib.utils.room.dao.HistoryRecordDao
import com.example.redrockai.lib.utils.room.db.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


/**
 *  author : lytMoon
 *  email : yytds@foxmail.com
 *  date: 2024/6/4 22:27
 *  version : 1.0
 *  description :
 *  saying : 这世界天才那么多，也不缺我一个
 */
class MineViewModel : ViewModel() {
    //room
    private lateinit var historyRecordDao: HistoryRecordDao

    var studyCourseNum: Int? = 0
    private var studyAllTime: Long? = 0


    //所有打包的数据
    private val _mineData = MutableStateFlow<MineData>(MineData())
    val mineData: StateFlow<MineData> = _mineData.asStateFlow()


    init {
        iniRoom()
    }

    private fun iniRoom() {
        historyRecordDao = AppDatabase.getDatabaseSingleton().historyRecordDao()

    }


    //整合打包数据
    fun upData() {
        getStudyCourseNum()
        studyAllTime = StudyTimeUtils.getLastStudiedTime().toLong()
        _mineData.value = MineData(studyCourseNum = studyCourseNum, studyTimeAll = studyAllTime)
    }


    private fun getStudyCourseNum() {
        viewModelScope.launch {
            studyCourseNum = historyRecordDao.getAllRecords().size
        }

    }


}