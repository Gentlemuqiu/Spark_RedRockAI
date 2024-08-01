package com.example.redrockai.lib.utils

import android.app.Application

interface InitialManager {

    val application: Application

    val currentProcessName: String

    val isMainProcess: Boolean
        get() = currentProcessName == application.packageName
}