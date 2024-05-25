package com.example.redrock.module.video.net

import com.example.lib.net.ServiceCreator
import retrofit2.await

object RelatedNetWork {
    private val relatedService = ServiceCreator.create<RelatedService>()
    suspend fun getRelated(id: Int) = relatedService.getRelated(id).await()
}