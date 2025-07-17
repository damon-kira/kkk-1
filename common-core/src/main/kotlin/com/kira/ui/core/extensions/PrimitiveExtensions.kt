

package com.kira.ui.core.extensions

import android.content.res.Resources
import android.view.KeyEvent

// TODO move to common-ui
internal fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.keyCodeToChar(): Char {
    val charCode = when (this) {
        KeyEvent.KEYCODE_DPAD_LEFT -> 8592 // ←
        KeyEvent.KEYCODE_DPAD_RIGHT -> 8594 // →
        KeyEvent.KEYCODE_DEL -> 9003 // ⌫
        else -> KeyEvent(KeyEvent.ACTION_DOWN, this).unicodeChar
    }
    return charCode.toChar()
}