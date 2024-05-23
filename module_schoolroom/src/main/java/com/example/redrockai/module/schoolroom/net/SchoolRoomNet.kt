package com.example.redrockai.module.schoolroom.net

import com.example.lib.net.ServiceCreator
import retrofit2.await

object SchoolRoomNet {
    suspend fun getCategories() =
        ServiceCreator
            .create<CateGoryService>()
            .getCateGory()
            .await()

    suspend fun getIntroduce(id: Int) =
        ServiceCreator
            .create<RelatedCategoryService>()
            .getRelatedCategory(id)
            .await()

    suspend fun getImage() =
        ServiceCreator
            .create<ImageService>()
            .getImage()
            .await()

    suspend fun getSentence() =
        ServiceCreator
            .create<SentenceService>()
            .getSentence()
            .await()
}