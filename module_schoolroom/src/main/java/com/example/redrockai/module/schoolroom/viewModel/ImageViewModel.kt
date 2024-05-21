package com.example.redrockai.module.schoolroom.viewModel;

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redrockai.module.schoolroom.bean.ImageDateBean
import com.example.redrockai.module.schoolroom.net.SchoolRoomNet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * @Author : Severus (Li XiaoHui)
 * @Email : 1627812101@qq.com
 * @Date : on 2024-05-21 19:13.
 * @Description : 生活很难，但总有人在爱你。
 */
class ImageViewModel() : ViewModel() {
    private val _imageData = MutableLiveData<ImageDateBean>()
    val imageDate: LiveData<ImageDateBean> get() = _imageData
    fun getImage() {
        viewModelScope.launch {
            flow {
                val list = SchoolRoomNet.getImage()
                emit(list)
            }.flowOn(Dispatchers.IO)
                .catch { e ->
                    e.printStackTrace()
                    Log.d("hui", "getImage: ${e}")
                }
                .collect {
                    _imageData.value = it
                }
        }
    }
}
