package com.example.redrockai.lib.utils

import android.app.Application

class UtilsApplicationWrapper : InitialService {
  
  companion object {
    internal lateinit var application: Application
      private set
  }

  override fun onAllProcess(manager: InitialManager) {
    application = manager.application
  }
}