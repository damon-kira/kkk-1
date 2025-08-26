package com.common.kira.ui.extensions

import android.graphics.Color
import androidx.compose.ui.graphics.Color as ComposeColor

fun ComposeColor.toHexString(): String {
    val red = this.red * 255
    val green = this.green * 255
    val blue = this.blue * 255
    val alpha = this.alpha * 255
    return String.format("#%02x%02x%02x%02x", alpha.toInt(), red.toInt(), green.toInt(), blue.toInt())
}

fun Int.toHexString(fallbackColor: String = "#FFFFFF"): String {
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