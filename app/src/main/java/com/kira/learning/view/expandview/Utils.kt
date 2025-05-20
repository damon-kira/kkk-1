package com.kira.learning.view.expandview

import android.content.Context
import android.util.DisplayMetrics

object Utils {
    fun convertPixelsToDp(px: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        val dp = px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        return dp
    }

    fun convertDpToPixels(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
}
