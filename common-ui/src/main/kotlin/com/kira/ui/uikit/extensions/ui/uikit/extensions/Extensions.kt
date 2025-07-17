package com.kira.ui.uikit.extensions.ui.uikit.extensions

import android.content.res.Resources
import android.graphics.Color

fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
fun Int.pxToDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.toHexString(fallbackColor: String = "#000000"): String {
    return try {
        "#" + Integer.toHexString(this)
    } catch (e: Exception) {
        fallbackColor
    }
}

fun Int.isColorDark(threshold: Double = 0.5): Boolean {
    if (this == Color.TRANSPARENT) {
        return false
    }
    val darkness = 1 - (
            0.299 * Color.red(this) +
                    0.587 * Color.green(this) +
                    0.114 * Color.blue(this)
            ) / 255
    return darkness >= threshold
}