package com.lytmoon.identify

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

interface ApiService {
    @POST("v1/private/hh_ocr_recognize_doc")
    @Headers("Content-Type: application/json")
    fun getData(
        @Query("authorization") authorization: String,
        @Query("host") host: String,
        @Query("date") date: String,
        @Body body: RequestParameterBody
    ): Call<RecognizeDocumentResponse>
}