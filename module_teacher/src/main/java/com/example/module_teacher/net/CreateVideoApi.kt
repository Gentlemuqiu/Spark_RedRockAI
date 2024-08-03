package com.example.module_teacher.net

import com.example.module_teacher.bean.CreateVideoRequest
import com.example.module_teacher.bean.CreateVideoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @Author      : Severus (Li XiaoHui)
 * @Email       : 1627812101@qq.com
 * @Date        : on 2024-08-03 14:31.
 * @Description : 生活很难，但总有人在爱你。
 */


interface CreateVideoApi {
    @POST("create_video")
    suspend fun CreateVideo(@Body requestBody: CreateVideoRequest): Response<CreateVideoResponse>
}