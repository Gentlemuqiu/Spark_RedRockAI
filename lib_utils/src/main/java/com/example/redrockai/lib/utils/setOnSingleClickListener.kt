package com.example.redrockai.lib.utils

import android.view.View

fun View.setOnSingleClickListener(interval: Long = 500, click: (View) -> Unit) {
  setOnClickListener {
    val tag = getTag(R.id.utils_single_click_id) as? Long
    if (System.currentTimeMillis() - (tag ?: 0L) > interval) {
      click(it)
    }
    it.setTag(R.id.utils_single_click_id, System.currentTimeMillis())
  }
}