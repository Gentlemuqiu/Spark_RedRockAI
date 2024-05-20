package com.example.redrockai.module.schoolroom.ui.fragment.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redrockai.module.schoolroom.ui.fragment.Bean.CateGoryBean
import com.example.redrockai.module.schoolroom.ui.fragment.net.SchoolRoomNet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


class CateGoryViewModel() : ViewModel() {
    private val _cateGoryData = MutableLiveData<CateGoryBean>()
    val cateGoryData: LiveData<CateGoryBean> get() = _cateGoryData
    fun getCateGory() {
        viewModelScope.launch {
            flow {
                val list = SchoolRoomNet.getCategories()
                emit(list)
            }.flowOn(Dispatchers.IO)
                .catch { e ->
                    e.printStackTrace()
                    Log.d("hui", "getCateGory: ${e}")
                }
                .collect {
                    _cateGoryData.value = it
                }
        }
    }
}