package com.colombia.credit.expand

import android.os.Build


fun isEmpty(str: String?): Boolean {
    return str == null || str.isEmpty()
}

fun isNotEmpty(str: String?): Boolean {
    return str != null && str.isNotEmpty()
}

/**
 * 是否是小米手机
 */
inline fun isXiaomi(): Boolean {
    return Build.BRAND.toUpperCase() == "XIAOMI"
}
