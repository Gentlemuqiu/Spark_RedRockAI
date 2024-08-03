package com.example.redrockai.module.schoolroom.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redrockai.module.schoolroom.bean.RelatedCategoryBean
import com.example.redrockai.module.schoolroom.net.SchoolRoomNet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class IntroduceViewModel() : ViewModel() {
    private val _relatedCategoryData = MutableLiveData<RelatedCategoryBean>()
    val relatedCategoryData: LiveData<RelatedCategoryBean> get() = _relatedCategoryData

    fun getRelatedCategoryData(id: Int) {
        viewModelScope.launch {
            flow {
                val list = SchoolRoomNet.getIntroduce(id)
                emit(list)
            }.flowOn(Dispatchers.IO)
                .catch { e ->
                    e.printStackTrace()
                    Log.d("hui", "getMonthRanking:${e} ")
                }
                .collect {
                    _relatedCategoryData.value = it
                    Log.d("33452352435", "测试数据${_relatedCategoryData.value}")

                }
        }
    }
}