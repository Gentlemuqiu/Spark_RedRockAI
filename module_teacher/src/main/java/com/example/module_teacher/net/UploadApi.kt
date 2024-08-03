package com.example.module_teacher.net

import com.example.module_teacher.bean.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadApi {
    @Multipart
    @POST("/upload")
    suspend fun uploadFile(@Part file: MultipartBody.Part): Response<UploadResponse>
}
