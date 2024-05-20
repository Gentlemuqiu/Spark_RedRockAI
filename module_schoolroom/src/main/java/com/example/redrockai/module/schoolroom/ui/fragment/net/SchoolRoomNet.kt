package com.example.redrockai.module.schoolroom.ui.fragment.net

import com.example.lib.net.ServiceCreator
import retrofit2.await

object SchoolRoomNet {
    private val cateGoryService = ServiceCreator.create<CateGoryService>()
    suspend fun getCategories() = cateGoryService.getCateGory().await()

    private val introduceService = ServiceCreator.create<getRelatedCategory>()
    suspend fun getIntroduce(id: Int) = introduceService.getRelatedCategory(id).await()
}