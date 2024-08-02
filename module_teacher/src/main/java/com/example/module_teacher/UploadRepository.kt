package com.example.module_teacher

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UploadRepository {
    private val uploadApi: UploadApi = RetrofitClient.instance.create(UploadApi::class.java)

    suspend fun uploadFile(file: File): UploadResponse? {
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        return withContext(Dispatchers.IO) {
            val response = uploadApi.uploadFile(body)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
    }
}
