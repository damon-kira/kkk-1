package com.util.lib.log

import android.util.Log
import com.util.lib.UtilInit


fun isDebug(): Boolean = UtilInit.get().isDebug()

inline fun debug(run: () -> Unit) {
    if (isDebug()) {
        run()
    }
}

inline fun logger_v(tag: String, message: String) {
    if (isDebug()) {
        Log.v(tag, message)
    }
}

inline fun logger_i(tag: String, message: String) {
    if (isDebug()) {
        Log.i(tag, message)
    }
}

inline fun logger_d(tag: String, message: String) {
    if (isDebug()) {
        Log.d(tag, message)
    }
}

fun logger_w(tag: String, message: String) {
    if (isDebug()) {
        Log.w(tag, message)
    }
}

inline fun logger_e(tag: String, message: String) {
    if (isDebug()) {
        Log.e(tag, message)
    }
}