package com.example.module_teacher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.module_teacher.bean.CreateVideoRequest
import com.example.module_teacher.bean.CreateVideoResponse
import com.example.module_teacher.repository.UploadRepository
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-08-03 14:48.
 * @Description : 生活很难，但总有人在爱你。
 */
class CreateVideoModel : ViewModel() {
    private val repository = UploadRepository()

    // LiveData 用于观察网络请求结果
    private val _response = MutableLiveData<Response<CreateVideoResponse>>()
    val response: LiveData<Response<CreateVideoResponse>> get() = _response

    fun createVideo(requestBody: CreateVideoRequest) {
        viewModelScope.launch {
            try {
                val result = repository.createVideo(requestBody)
                _response.value = result
            } catch (e: Exception) {
                // 处理异常
                e.printStackTrace()
            }
        }
    }
}