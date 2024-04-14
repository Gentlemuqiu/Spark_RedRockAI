package com.example.redrockai.lib.utils

import android.app.Application
import android.content.Context

open class BaseApp : Application() {


    companion object {
        var instance: BaseApp? = null
        fun getAppContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


}