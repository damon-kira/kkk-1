package com.colombia.credit.module.ocr.utils

import android.content.Context
import android.util.DisplayMetrics

/**
 * Created by Administrator on 2016/12/8.
 */
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
