package com.example.redrockai.module.schoolroom.net

import retrofit2.await

object SchoolRoomNet {
    suspend fun getCategories() =
        CateGoryService
            .create<CateGoryService>()
            .getCateGory()
            .await()

    suspend fun getIntroduce(id: Int) =
        CateGoryService
            .create<getRelatedCategory>()
            .getRelatedCategory(id)
            .await()
}