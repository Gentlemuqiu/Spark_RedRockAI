package com.lytmoon.identify

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


class MainActivity : AppCompatActivity() {
    var authorization: String? = null
    var host: String? = null
    var date: String? = null


    @SuppressLint("WrongThread")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.hanyv)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val encodedImage = Base64.getEncoder().encodeToString(byteArray)


        val ocr = OCR.getInstance(encodedImage)

        Thread {
            val result = ocr.ocrResult
            Log.d("wfefwdfwefew", "测试数据${result.toString()}")
        }.start()


        val result =
            buildRequestUrl(
                "https://api.xf-yun.com/v1/private/hh_ocr_recognize_doc",
                "OGY2M2FhMGJiMTMxN2QzMWRiYTZjMzc5",
                "7a1bbccf7648b6a20218ea30eb529db5",
            )
        Log.d("wfioewjfwef", "测试数据$result")

//        testRetrofit(result, encodedImage)


    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun buildRequestUrl(
        requestUrl: String,
        apiSecret: String,
        apiKey: String,
    ): String {
        val httpRequestUrl = requestUrl.replace("ws://", "http://").replace("wss://", "https://")
        try {
            val url = URL(httpRequestUrl)
            val format = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
            format.timeZone = TimeZone.getTimeZone("GMT")
            val date = format.format(Date())
            val host = url.host
            val builder = StringBuilder("host: ").append(host).append("\n")
                .append("date: ").append(date).append("\n")
                .append("POST ").append(url.path).append(" HTTP/1.1")
            val charset = Charsets.UTF_8
            val mac = Mac.getInstance("HmacSHA256")
            val spec = SecretKeySpec(apiSecret.toByteArray(charset), "HmacSHA256")
            mac.init(spec)
            val hexDigits = mac.doFinal(builder.toString().toByteArray(charset))
            val sha = Base64.getEncoder().encodeToString(hexDigits)
            val authorization = String.format(
                "api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"",
                apiKey, "hmac-sha256", "host date request-line", sha
            )
            val authBase = Base64.getEncoder().encodeToString(authorization.toByteArray(charset))

            this.authorization = URLEncoder.encode(authBase, charset.name())
            this.host = URLEncoder.encode(host, charset.name())
            this.date = URLEncoder.encode(date, charset.name())
            Log.d("LogTest", "authorization${this.authorization}")
            Log.d("LogTest", "host${this.host}")
            Log.d("LogTest", "date${this.date}")



            return String.format(
                "%s?authorization=%s&host=%s&date=%s",
                requestUrl,
                URLEncoder.encode(authBase, charset.name()),
                URLEncoder.encode(host, charset.name()),
                URLEncoder.encode(date, charset.name())
            )
        } catch (e: Exception) {
            throw RuntimeException("assemble requestUrl error:" + e.message)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun testRetrofit(baseurl: String, encodedImage: String) {


        val requestBody = RequestParameterBody(
            header = Header(
                app_id = "82d5a760",
                status = 3
            ),
            parameter = Parameter(
                hh_ocr_recognize_doc = OcrRecognizeDoc(
                    recognizeDocumentRes = RecognizeDocumentRes(
                        encoding = "utf8",
                        compress = "raw",
                        format = "json"
                    )
                )
            ),
            payload = Payload(
                image = Image(
                    encoding = "jpg",
                    image = encodedImage, // 这里是图片的Base64编码
                    status = 3
                )
            )
        )

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.xf-yun.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())//这里添加GSON的converter,后面把数据解析成对象要用。
            .build()
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getData(
            authorization.toString(),
            host.toString(),
            date.toString(),
            requestBody
        )


        call.enqueue(object : Callback<RecognizeDocumentResponse> {
            override fun onResponse(
                call: Call<RecognizeDocumentResponse>,
                response: Response<RecognizeDocumentResponse>
            ) {
                val list = response.body()
                Log.d("fqwdqwdwqd", "测试数据${list}")

            }

            override fun onFailure(call: retrofit2.Call<RecognizeDocumentResponse>, t: Throwable) {
//                t.printStackTrace()
                Log.d("wdqwdwqdwq", "测试数据${t.message}")

            }
        })
    }


    class LoggingInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request = chain.request()
            val url = request.url().toString()
            Log.d("fwfwefwef", "测试数据${url}")
            return chain.proceed(request)
        }
    }


}


