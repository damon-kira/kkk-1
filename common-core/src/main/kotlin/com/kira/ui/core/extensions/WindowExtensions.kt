

package com.kira.ui.core.extensions

import android.app.Activity
import android.view.Window
import android.widget.EditText
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

fun Window.fullscreenMode(whether: Boolean) {
    val controller = WindowCompat.getInsetsController(this, decorView)
    val statusBarType = WindowInsetsCompat.Type.statusBars()
    if (whether) {
        controller.hide(statusBarType)
    } else {
        controller.show(statusBarType)
    }
}

fun Window.decorFitsSystemWindows(decorFitsSystemWindows: Boolean) {
    WindowCompat.setDecorFitsSystemWindows(this, decorFitsSystemWindows)
}

fun Activity.focusedTextField(): EditText? {
    val currentFocusView = currentFocus
    if (currentFocusView is EditText) {
        return currentFocusView
    }
    return null
}