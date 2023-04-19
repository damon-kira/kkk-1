package com.colombia.credit.expand

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import com.colombia.credit.BuildConfig
import com.colombia.credit.LoanApplication.Companion.getAppContext


/**
 * 是否是小米手机
 */
inline fun isXiaomi(): Boolean {
    return Build.BRAND.toUpperCase() == "XIAOMI"
}

fun copyClick(copyText: String) {
    if (copyText.isEmpty()) {
        return
    }
    val myClipboard =
        getAppContext()
            .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val myClip = ClipData.newPlainText("text", copyText)
    try {
        myClipboard.setPrimaryClip(myClip)
    } catch (e: SecurityException) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace()
            throw e
        }
    }
}
