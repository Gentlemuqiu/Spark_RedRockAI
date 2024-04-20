package com.lytmoon.identify

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.api.orc.OCR
import com.example.api.rxjava.SwitchThread.ioToMainThread
import com.example.api.orc.dataBean.DataClass
import java.io.ByteArrayOutputStream
import java.util.Base64

/**
 * author : lytMoon
 * date: 2024/4/19 12:52
 * version : 1.0
 * description :
 * saying : 这世界天才那么多，也不缺我一个
 */
class MainActivity : AppCompatActivity() {

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



        ioToMainThread<DataClass>({ ocr.ocrResult }) {


            Log.d("WFAWFAWFWE", "测试数据${it}")
        }
    }
}

private fun Unit.observeOnUI(function: () -> Int) {

}




