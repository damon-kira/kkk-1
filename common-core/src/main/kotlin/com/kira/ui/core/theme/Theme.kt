

package com.kira.ui.core.theme

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

enum class Theme(val value: String) {
    LIGHT("light"),
    DARK("dark"),
    SYSTEM_DEFAULT("system_default");

    fun apply() {
        val mode = when (this) {
            LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            DARK -> AppCompatDelegate.MODE_NIGHT_YES
            SYSTEM_DEFAULT -> if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            } else {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    companion object {

        fun of(value: String): Theme {
            return values().find { it.value == value } ?: DARK
        }
    }
}