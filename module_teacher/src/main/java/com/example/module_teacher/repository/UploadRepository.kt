package com.example.module_teacher.repository

import com.example.module_teacher.bean.CreateVideoRequest
import com.example.module_teacher.bean.CreateVideoResponse
import com.example.module_teacher.bean.UploadResponse
import com.example.module_teacher.net.CreateVideoApi
import com.example.module_teacher.net.RetrofitClient
import com.example.module_teacher.net.UploadApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

class UploadRepository {
    private val uploadApi: UploadApi = RetrofitClient.instance.create(UploadApi::class.java)

    suspend fun uploadFile(file: File): UploadResponse? {
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("jpg", file.name, requestFile)

        return withContext(Dispatchers.IO) {
            val response = uploadApi.uploadFile(body)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
    }

    suspend fun uploadFileVideo(file: File): UploadResponse? {
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("mp4", file.name, requestFile)
        return withContext(Dispatchers.IO) {
            val response = uploadApi.uploadFile(body)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
    }

    private val createVideoApi: CreateVideoApi =
        RetrofitClient.instance.create(CreateVideoApi::class.java)


    suspend fun createVideo(requestBody: CreateVideoRequest): Response<CreateVideoResponse> {
        return createVideoApi.CreateVideo(requestBody)
    }
}
