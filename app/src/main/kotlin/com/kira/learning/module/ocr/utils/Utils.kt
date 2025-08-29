package com.kira.learning.module.ocr.utils

import android.content.Context
import android.util.DisplayMetrics

object Utils {
    fun getScreenWH(context: Context): DisplayMetrics? {
        var dMetrics: DisplayMetrics? = DisplayMetrics()
        dMetrics = context.getResources().getDisplayMetrics()
        return dMetrics
    }

    fun getWidthInPx(context: Context): Int {
        val width = context.getResources().getDisplayMetrics().widthPixels
        return width
    }
}
