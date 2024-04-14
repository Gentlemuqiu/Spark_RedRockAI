package com.example.redrockai.lib.utils


import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.widget.Toast

object BaseUtils {


    private val mainHandler = Handler(Looper.getMainLooper())

    fun shortToast(shortText: String) {
        mainHandler.post {
            Toast.makeText(BaseApp.getAppContext(), shortText, Toast.LENGTH_SHORT).show()
        }
    }

    fun longToast(longText: String) {
        mainHandler.post {
            Toast.makeText(BaseApp.getAppContext(), longText, Toast.LENGTH_LONG).show()
        }
    }

    fun startTransParentBar(window: Window) {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }

}