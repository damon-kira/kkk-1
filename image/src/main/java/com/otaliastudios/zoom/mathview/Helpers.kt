package com.otaliastudios.zoom.mathview

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.net.URLEncoder

object Helpers {

    fun encode(url: String?): String = URLEncoder.encode("\\Huge $url", "utf-8")

    fun Context.isDarkMode(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            this.resources?.configuration?.uiMode
                ?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        } else {
            false
        }

    inline fun <reified T : Enum<T>> safeValueOf(type: String, default: T): T {
        return try {
            val name = type.map { if (it.isLetterOrDigit()) it.uppercase() else "_" }
                .joinToString("")
            java.lang.Enum.valueOf(T::class.java, name)
        } catch (e: IllegalArgumentException) {
            default
        }
    }
}
