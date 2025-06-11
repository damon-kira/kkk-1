package com.datepicker.lib

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

object Util {
    /**
     * get screen width.
     */
    @JvmStatic
    fun getScreenWidth(context: Activity): Float {
        val dm = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels.toFloat()
    }

    /**
     * dp to pix
     */
    fun dpToPx(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * pix to dp
     */
    fun pxToDp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * sp to pix
     */
    fun spToPx(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * pix to sp
     */
    @JvmStatic
    fun pxToSp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }
}
