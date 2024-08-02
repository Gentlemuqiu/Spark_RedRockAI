package com.example.module_teacher

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File

class UploadViewModel : ViewModel() {
    private val repository = UploadRepository()
    private val _uploadResponse = MutableLiveData<UploadResponse?>()
    val uploadResponse: LiveData<UploadResponse?> get() = _uploadResponse
    private val _uploadResponseVideo = MutableLiveData<UploadResponse?>()
    val uploadResponseVideo: LiveData<UploadResponse?> get() = _uploadResponseVideo
    fun uploadFile(file: File) {
        viewModelScope.launch {
            try {
                val response = repository.uploadFile(file)
                _uploadResponse.postValue(response)
            }catch (e: Exception) {
                // Handle the exception
                Log.e("NetworkError", "Error fetching data: ", e)
            }

        }
    }

    fun uploadFileVideo(file: File) {
        viewModelScope.launch {
            try {
                val response = repository.uploadFile(file)
                _uploadResponseVideo.postValue(response)
            }catch (e: Exception) {
                // Handle the exception
                Log.e("NetworkError", "Error fetching data: ", e)
            }
        }
    }
}
